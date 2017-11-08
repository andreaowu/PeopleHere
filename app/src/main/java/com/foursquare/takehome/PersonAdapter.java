package com.foursquare.takehome;

import android.content.Context;
import android.support.annotation.VisibleForTesting;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

final public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.ViewHolder> {

    private List<Person> visitors;
    private Context context;

    public PersonAdapter(Context context) {
        this.context = context;
        visitors = new ArrayList<>();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameView;
        public TextView timeView;

        public ViewHolder(View itemView) {
            super(itemView);

            nameView = (TextView) itemView.findViewById(R.id.visitor_name);
            timeView = (TextView) itemView.findViewById(R.id.visitor_visittime);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.visitor, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Person person = visitors.get(position);

        TextView name = holder.nameView;
        name.setText(person.getName());

        TextView time = holder.timeView;
        DateFormat formatter = new SimpleDateFormat("hh:mm a");
        time.setText(formatter.format(new Date(person.getArriveTime() * 1000))
                + " - " + formatter.format(new Date(person.getLeaveTime() * 1000)));

        if (person.getName().equals(context.getResources().getString(R.string.no_visitors))) {
            int color = context.getResources().getColor(R.color.lighter_gray);
            name.setTextColor(color);
            time.setTextColor(color);
        }
    }

    @Override
    public int getItemCount() {
        return visitors.size();
    }

    public void setVisitors(Venue venue) {
        // Get list of visitors at the venue
        List<Person> visitorsList = venue.getVisitors();

        // If there are no visitors, don't do anything
        if (visitorsList == null || visitorsList.isEmpty()) {
            return;
        }

        // Tracks the intervals for which there are visitors at the venue
        ArrayList<long[]> storeVisitorTimes = new ArrayList<>();

        // Get first person in the visitorsList and add it to lists
        Person person = visitorsList.get(0);
        visitors.add(person);
        storeVisitorTimes.add(new long[] {person.getArriveTime(), person.getLeaveTime()});

        // Iterate through the visitors list once
        for (int i = 1; i < visitorsList.size(); i++) {
            person = visitorsList.get(i);

            // Add visitor into the visitors list, sorted by arrival time
            addToVisitors(binarySearchInsert(person), person);

            // Account for visitor's time in storeVisitorTimes
            storeVisitorTimes = updateStoreVisitorTimes(storeVisitorTimes, person);
        }

        // Add no visitors Persons where there are time gaps
        visitors = addNoVisitors(storeVisitorTimes, venue.getOpenTime(), venue.getCloseTime());
    }

    @VisibleForTesting
    // Keep visitors sorted by time, and do binary search to find index at which to insert given person
    public int binarySearchInsert(Person person) {
        int visitorsSize = visitors.size();
        int low = 0;
        int high = visitorsSize == 1 ? 1 : visitorsSize - 1;
        int mid = (low + high) / 2;
        long arriveTime = person.getArriveTime();
        while (low < high) {
            mid = (low + high) / 2;
            long midArriveTime = visitors.get(mid).getArriveTime();
            long midLeaveTime = visitors.get(mid).getLeaveTime();
            if (midArriveTime == arriveTime && midLeaveTime == person.getLeaveTime()) {
                break;
            }
            if (midArriveTime < arriveTime) {
                boolean add = checkMid(low, high, mid, visitorsSize, arriveTime);
                low = Math.min(visitorsSize, mid + 1);
                mid = Math.min(low + (add ? 1 : 0), visitorsSize);
            } else if (midArriveTime > arriveTime) {
                high = mid - 1 < 0 ? 0 : mid - 1;
            } else {
                if (midLeaveTime < person.getLeaveTime()) {
                    boolean add = checkMid(low, high, mid, visitorsSize, arriveTime);
                    low = Math.min(visitorsSize, mid + 1);
                    mid = Math.min(low + (add ? 1 : 0), visitorsSize);
                } else {
                    high = mid - 1 < 0 ? 0 : mid - 1;
                }
            }
        }
        return mid;
    }

    private boolean checkMid(int low, int high, int mid, int visitorsSize, long arriveTime) {
        boolean add = false;
        if ( (low + high % 2) != 0) {
            // This means the 'middle' could be mid or mid + 1, so check
            if (mid == visitorsSize - 1
                    || (mid + 1 < visitorsSize && visitors.get(mid + 1).getArriveTime() < arriveTime)) {
                add = true;
            }
        }
        return add;
    }

    @VisibleForTesting
    public void addToVisitors(int index, Person person) {
        visitors.add(index < 0 ? 0 : index, person);
    }

    @VisibleForTesting
    public ArrayList<long[]> updateStoreVisitorTimes(ArrayList<long[]> storeVisitorTimes, Person person) {
        // Keep track of whether person's times are already accounted for
        boolean alreadyAdded = false;

        // Get person's arrive and leave times
        long personArriveTime = person.getArriveTime();
        long personLeaveTime = person.getLeaveTime();

        // Iterate through venue's visitor times
        // Check whether person's visiting time window is already present and add it if not
        for (int j = 0; j < storeVisitorTimes.size(); j++) {
            // Current time window being checked
            long checkArriveTime = storeVisitorTimes.get(j)[0];
            long checkLeaveTime = storeVisitorTimes.get(j)[1];

            if (personArriveTime >= checkArriveTime && personLeaveTime <= checkLeaveTime) {
                // Person's arrive and leave time is exactly the same as the current time window being checked
                alreadyAdded = true;
            }
            if (personArriveTime < checkArriveTime) {
                if (personLeaveTime < checkArriveTime) {
                    // Both arrive time and leave time are less than current timeframe's arrive time
                    // If person's leave time is less than checkArriveTime, then person's arrive time
                    // must be less than checkArriveTime so there's no issue with adding a new entry
                    storeVisitorTimes.add(j, new long[]{personArriveTime, personLeaveTime});
                } else if (personLeaveTime <= checkLeaveTime) {
                    // Arrive time is less than current timeframe's arrive time but leave time is
                    // within range of current timeframe's time
                    // This also takes care of the case where person's get leave time equals checkArriveTime
                    storeVisitorTimes.get(j)[0] = personArriveTime;
                } else {
                    // Person's leave time is greater than checkLeaveTime which means the current person's
                    // time spans across this storeVisitorTime
                    storeVisitorTimes.get(j)[0] = personArriveTime;
                    storeVisitorTimes.get(j)[1] = personLeaveTime;
                }
                alreadyAdded = true;
            } else if (personArriveTime <= checkLeaveTime) {
                // Person's arrive time is between checkArriveTime and checkLeaveTime
                storeVisitorTimes.get(j)[1] = Math.max(personLeaveTime, checkLeaveTime);
                alreadyAdded = true;
            }

            if (alreadyAdded) {
                if (j != 0 && personArriveTime <= storeVisitorTimes.get(j - 1)[0]) {
                    // Chain blocks of time together if the current timeframe overlaps with the one before it
                    storeVisitorTimes.get(j - 1)[1] = checkLeaveTime;
                    storeVisitorTimes.remove(j);
                }
                if (j != storeVisitorTimes.size() - 1 && personLeaveTime >= storeVisitorTimes.get(j + 1)[0]) {
                    // Chain blocks of time together if the current timeframe overlaps with the one after it
                    storeVisitorTimes.get(j + 1)[0] = checkArriveTime;
                    storeVisitorTimes.remove(j);
                }
                break;
            }
        }

        if (!alreadyAdded) {
            // Went through entire list without being added, which means person's arrive time
            // was always beyond the checkLeaveTime
            storeVisitorTimes.add(new long[] { personArriveTime, personLeaveTime });
        }

        return storeVisitorTimes;
    }

    private List<Person> addNoVisitors(ArrayList<long[]> storeVisitorTimes,
                                            long openTime, long closeTime) {
        // Check whether beginning of visitors starts at venue's open and close times
        if (storeVisitorTimes.get(0)[0] != openTime) {
            visitors.add(0, makeNoVisitorPerson(openTime, storeVisitorTimes.get(0)[0]));
        }
        if (storeVisitorTimes.get(storeVisitorTimes.size() - 1)[1] != closeTime) {
            visitors.add(visitors.size(),
                    makeNoVisitorPerson(storeVisitorTimes.get(storeVisitorTimes.size() - 1)[1], closeTime));
        }

        // Find the intervals without visitors at the venue, and insert new 'no visitor' Person into
        // visited list via binary search
        for (int i = 1; i < storeVisitorTimes.size(); i++) {
            Person person = makeNoVisitorPerson(storeVisitorTimes.get(i - 1)[1], storeVisitorTimes.get(i)[0]);
            int insertIndex = binarySearchInsert(person);
            visitors.add(insertIndex < 0 ? 0 : insertIndex, person);
        }

        return visitors;
    }

    // Make a new 'no visitor' person
    private Person makeNoVisitorPerson(long arrive, long leave) {
        Person person = new Person();
        person.setArriveTime((int) arrive);
        person.setLeaveTime((int) leave);
        person.setName(context.getResources().getString(R.string.no_visitors));
        return person;
    }
}
