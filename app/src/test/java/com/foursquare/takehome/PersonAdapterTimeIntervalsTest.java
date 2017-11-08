package com.foursquare.takehome;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by andreawu on 11/7/17.
 */
public class PersonAdapterTimeIntervalsTest {

    @Mock
    Context context;

    PersonAdapter personAdapter;
    ArrayList<long[]> storeVisitorTimes;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        personAdapter = new PersonAdapter(context);
        storeVisitorTimes = new ArrayList<>();
    }


    @Test
    public void emptyList() {
        Person personOne = new Person();
        personOne.setArriveTime(1);
        personOne.setLeaveTime(2);
        storeVisitorTimes = personAdapter.updateStoreVisitorTimes(storeVisitorTimes, personOne);

        assertEquals(1, storeVisitorTimes.size());
        assertEquals(1, storeVisitorTimes.get(0)[0]);
        assertEquals(2, storeVisitorTimes.get(0)[1]);
    }

    @Test
    public void addNonOverlappingIncreasing() {
        Person personOne = new Person();
        personOne.setArriveTime(1);
        personOne.setLeaveTime(2);
        storeVisitorTimes = personAdapter.updateStoreVisitorTimes(storeVisitorTimes, personOne);

        assertEquals(1, storeVisitorTimes.size());
        assertEquals(1, storeVisitorTimes.get(0)[0]);
        assertEquals(2, storeVisitorTimes.get(0)[1]);

        Person personTwo = new Person();
        personTwo.setArriveTime(3);
        personTwo.setLeaveTime(4);
        storeVisitorTimes = personAdapter.updateStoreVisitorTimes(storeVisitorTimes, personTwo);

        assertEquals(2, storeVisitorTimes.size());
        assertEquals(3, storeVisitorTimes.get(1)[0]);
        assertEquals(4, storeVisitorTimes.get(1)[1]);
    }

    @Test
    public void addNonOverlappingDecreasing() {
        Person personOne = new Person();
        personOne.setArriveTime(3);
        personOne.setLeaveTime(4);
        storeVisitorTimes = personAdapter.updateStoreVisitorTimes(storeVisitorTimes, personOne);

        assertEquals(1, storeVisitorTimes.size());
        assertEquals(3, storeVisitorTimes.get(0)[0]);
        assertEquals(4, storeVisitorTimes.get(0)[1]);

        Person personTwo = new Person();
        personTwo.setArriveTime(1);
        personTwo.setLeaveTime(2);
        storeVisitorTimes = personAdapter.updateStoreVisitorTimes(storeVisitorTimes, personTwo);

        assertEquals(2, storeVisitorTimes.size());
        assertEquals(1, storeVisitorTimes.get(0)[0]);
        assertEquals(2, storeVisitorTimes.get(0)[1]);
    }

    @Test
    public void addEqual() {
        Person personOne = new Person();
        personOne.setArriveTime(1);
        personOne.setLeaveTime(2);
        storeVisitorTimes = personAdapter.updateStoreVisitorTimes(storeVisitorTimes, personOne);

        assertEquals(1, storeVisitorTimes.size());
        assertEquals(1, storeVisitorTimes.get(0)[0]);
        assertEquals(2, storeVisitorTimes.get(0)[1]);

        Person personTwo = new Person();
        personTwo.setArriveTime(1);
        personTwo.setLeaveTime(2);
        storeVisitorTimes = personAdapter.updateStoreVisitorTimes(storeVisitorTimes, personTwo);

        assertEquals(1, storeVisitorTimes.size());
        assertEquals(1, storeVisitorTimes.get(0)[0]);
        assertEquals(2, storeVisitorTimes.get(0)[1]);
    }

    @Test
    public void addOverlapArrival() {
        Person personOne = new Person();
        personOne.setArriveTime(1);
        personOne.setLeaveTime(2);
        storeVisitorTimes = personAdapter.updateStoreVisitorTimes(storeVisitorTimes, personOne);

        assertEquals(1, storeVisitorTimes.size());
        assertEquals(1, storeVisitorTimes.get(0)[0]);
        assertEquals(2, storeVisitorTimes.get(0)[1]);

        Person personTwo = new Person();
        personTwo.setArriveTime(1);
        personTwo.setLeaveTime(5);
        storeVisitorTimes = personAdapter.updateStoreVisitorTimes(storeVisitorTimes, personTwo);

        assertEquals(1, storeVisitorTimes.size());
        assertEquals(1, storeVisitorTimes.get(0)[0]);
        assertEquals(5, storeVisitorTimes.get(0)[1]);
    }

    @Test
    public void addOverlapDeparture() {
        Person personOne = new Person();
        personOne.setArriveTime(5);
        personOne.setLeaveTime(8);
        storeVisitorTimes = personAdapter.updateStoreVisitorTimes(storeVisitorTimes, personOne);

        assertEquals(1, storeVisitorTimes.size());
        assertEquals(5, storeVisitorTimes.get(0)[0]);
        assertEquals(8, storeVisitorTimes.get(0)[1]);

        Person personTwo = new Person();
        personTwo.setArriveTime(1);
        personTwo.setLeaveTime(8);
        storeVisitorTimes = personAdapter.updateStoreVisitorTimes(storeVisitorTimes, personTwo);

        assertEquals(1, storeVisitorTimes.size());
        assertEquals(1, storeVisitorTimes.get(0)[0]);
        assertEquals(8, storeVisitorTimes.get(0)[1]);
    }

    @Test
    public void addOverlapArrivalLess() {
        Person personOne = new Person();
        personOne.setArriveTime(2);
        personOne.setLeaveTime(6);
        storeVisitorTimes = personAdapter.updateStoreVisitorTimes(storeVisitorTimes, personOne);

        assertEquals(1, storeVisitorTimes.size());
        assertEquals(2, storeVisitorTimes.get(0)[0]);
        assertEquals(6, storeVisitorTimes.get(0)[1]);

        Person personTwo = new Person();
        personTwo.setArriveTime(1);
        personTwo.setLeaveTime(4);
        storeVisitorTimes = personAdapter.updateStoreVisitorTimes(storeVisitorTimes, personTwo);

        assertEquals(1, storeVisitorTimes.size());
        assertEquals(1, storeVisitorTimes.get(0)[0]);
        assertEquals(6, storeVisitorTimes.get(0)[1]);
    }

    @Test
    public void addOverlapArrivalLessReverse() {
        Person personOne = new Person();
        personOne.setArriveTime(1);
        personOne.setLeaveTime(4);
        storeVisitorTimes = personAdapter.updateStoreVisitorTimes(storeVisitorTimes, personOne);

        assertEquals(1, storeVisitorTimes.size());
        assertEquals(1, storeVisitorTimes.get(0)[0]);
        assertEquals(4, storeVisitorTimes.get(0)[1]);

        Person personTwo = new Person();
        personTwo.setArriveTime(2);
        personTwo.setLeaveTime(6);
        storeVisitorTimes = personAdapter.updateStoreVisitorTimes(storeVisitorTimes, personTwo);

        assertEquals(1, storeVisitorTimes.size());
        assertEquals(1, storeVisitorTimes.get(0)[0]);
        assertEquals(6, storeVisitorTimes.get(0)[1]);
    }

    @Test
    public void addOverlapWithin() {
        Person personOne = new Person();
        personOne.setArriveTime(1);
        personOne.setLeaveTime(6);
        storeVisitorTimes = personAdapter.updateStoreVisitorTimes(storeVisitorTimes, personOne);

        assertEquals(1, storeVisitorTimes.size());
        assertEquals(1, storeVisitorTimes.get(0)[0]);
        assertEquals(6, storeVisitorTimes.get(0)[1]);

        Person personTwo = new Person();
        personTwo.setArriveTime(2);
        personTwo.setLeaveTime(4);
        storeVisitorTimes = personAdapter.updateStoreVisitorTimes(storeVisitorTimes, personTwo);

        assertEquals(1, storeVisitorTimes.size());
        assertEquals(1, storeVisitorTimes.get(0)[0]);
        assertEquals(6, storeVisitorTimes.get(0)[1]);
    }

    @Test
    public void addOverlapWithinReverse() {
        Person personOne = new Person();
        personOne.setArriveTime(2);
        personOne.setLeaveTime(4);
        storeVisitorTimes = personAdapter.updateStoreVisitorTimes(storeVisitorTimes, personOne);

        assertEquals(1, storeVisitorTimes.size());
        assertEquals(2, storeVisitorTimes.get(0)[0]);
        assertEquals(4, storeVisitorTimes.get(0)[1]);

        Person personTwo = new Person();
        personTwo.setArriveTime(1);
        personTwo.setLeaveTime(6);
        storeVisitorTimes = personAdapter.updateStoreVisitorTimes(storeVisitorTimes, personTwo);

        assertEquals(1, storeVisitorTimes.size());
        assertEquals(1, storeVisitorTimes.get(0)[0]);
        assertEquals(6, storeVisitorTimes.get(0)[1]);
    }

    @Test
    public void addOverlapDepartureMore() {
        Person personOne = new Person();
        personOne.setArriveTime(1);
        personOne.setLeaveTime(6);
        storeVisitorTimes = personAdapter.updateStoreVisitorTimes(storeVisitorTimes, personOne);

        assertEquals(1, storeVisitorTimes.size());
        assertEquals(1, storeVisitorTimes.get(0)[0]);
        assertEquals(6, storeVisitorTimes.get(0)[1]);

        Person personTwo = new Person();
        personTwo.setArriveTime(3);
        personTwo.setLeaveTime(8);
        storeVisitorTimes = personAdapter.updateStoreVisitorTimes(storeVisitorTimes, personTwo);

        assertEquals(1, storeVisitorTimes.size());
        assertEquals(1, storeVisitorTimes.get(0)[0]);
        assertEquals(8, storeVisitorTimes.get(0)[1]);
    }

    @Test
    public void addOverlapDepartureMoreReverse() {
        Person personOne = new Person();
        personOne.setArriveTime(3);
        personOne.setLeaveTime(8);
        storeVisitorTimes = personAdapter.updateStoreVisitorTimes(storeVisitorTimes, personOne);

        assertEquals(1, storeVisitorTimes.size());
        assertEquals(3, storeVisitorTimes.get(0)[0]);
        assertEquals(8, storeVisitorTimes.get(0)[1]);

        Person personTwo = new Person();
        personTwo.setArriveTime(2);
        personTwo.setLeaveTime(6);
        storeVisitorTimes = personAdapter.updateStoreVisitorTimes(storeVisitorTimes, personTwo);

        assertEquals(1, storeVisitorTimes.size());
        assertEquals(2, storeVisitorTimes.get(0)[0]);
        assertEquals(8, storeVisitorTimes.get(0)[1]);
    }

    @Test
    public void addConnect() {
        Person personOne = new Person();
        personOne.setArriveTime(1);
        personOne.setLeaveTime(2);
        storeVisitorTimes = personAdapter.updateStoreVisitorTimes(storeVisitorTimes, personOne);

        assertEquals(1, storeVisitorTimes.size());
        assertEquals(1, storeVisitorTimes.get(0)[0]);
        assertEquals(2, storeVisitorTimes.get(0)[1]);

        Person personTwo = new Person();
        personTwo.setArriveTime(2);
        personTwo.setLeaveTime(3);
        storeVisitorTimes = personAdapter.updateStoreVisitorTimes(storeVisitorTimes, personTwo);

        assertEquals(1, storeVisitorTimes.size());
        assertEquals(1, storeVisitorTimes.get(0)[0]);
        assertEquals(3, storeVisitorTimes.get(0)[1]);
    }
}