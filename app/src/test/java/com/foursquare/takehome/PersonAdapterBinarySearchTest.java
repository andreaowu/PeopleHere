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
public class PersonAdapterBinarySearchTest {

    @Mock
    Context context;

    PersonAdapter personAdapter;
    ArrayList<Person> visitors;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        personAdapter = new PersonAdapter(context);
        visitors = new ArrayList<>();
    }

    @Test
    public void binarySearchInsertOneVisitor() {
        Person person = new Person();
        person.setArriveTime(1);
        person.setLeaveTime(2);
        assertEquals(personAdapter.binarySearchInsert(person), 0);
    }

    @Test
    public void binarySearchInsertTwoVisitorsGreater() {
        Person personOne = new Person();
        personOne.setArriveTime(1);
        personOne.setLeaveTime(2);
        assertEquals(0, personAdapter.binarySearchInsert(personOne));

        personAdapter.addToVisitors(0, personOne);
        Person personTwo = new Person();
        personTwo.setArriveTime(3);
        personTwo.setLeaveTime(4);
        assertEquals(1, personAdapter.binarySearchInsert(personTwo));
    }

    @Test
    public void binarySearchInsertTwoVisitorsLess() {
        Person personOne = new Person();
        personOne.setArriveTime(3);
        personOne.setLeaveTime(4);
        assertEquals(0, personAdapter.binarySearchInsert(personOne));

        personAdapter.addToVisitors(0, personOne);
        Person personTwo = new Person();
        personTwo.setArriveTime(1);
        personTwo.setLeaveTime(2);
        assertEquals(0, personAdapter.binarySearchInsert(personTwo));
    }

    @Test
    public void binarySearchInsertTwoVisitorsEqual() {
        Person personOne = new Person();
        personOne.setArriveTime(1);
        personOne.setLeaveTime(2);
        assertEquals(0, personAdapter.binarySearchInsert(personOne));

        personAdapter.addToVisitors(0, personOne);
        Person personTwo = new Person();
        personTwo.setArriveTime(1);
        personTwo.setLeaveTime(2);
        assertEquals(0, personAdapter.binarySearchInsert(personTwo));
    }

    @Test
    public void binarySearchInsertTwoVisitorsEqualArriveLessDepart() {
        Person personOne = new Person();
        personOne.setArriveTime(1);
        personOne.setLeaveTime(2);
        assertEquals(0, personAdapter.binarySearchInsert(personOne));

        personAdapter.addToVisitors(0, personOne);
        Person personTwo = new Person();
        personTwo.setArriveTime(1);
        personTwo.setLeaveTime(3);
        assertEquals(1, personAdapter.binarySearchInsert(personTwo));
    }

    @Test
    public void binarySearchInsertTwoVisitorsEqualArriveMoreDepart() {
        Person personOne = new Person();
        personOne.setArriveTime(1);
        personOne.setLeaveTime(3);
        assertEquals(0, personAdapter.binarySearchInsert(personOne));

        personAdapter.addToVisitors(0, personOne);
        Person personTwo = new Person();
        personTwo.setArriveTime(1);
        personTwo.setLeaveTime(2);
        assertEquals(0, personAdapter.binarySearchInsert(personTwo));
    }

    @Test
    public void binarySearchInsertThreeVisitorsOrdered() {
        Person personOne = new Person();
        personOne.setArriveTime(1);
        personOne.setLeaveTime(2);
        assertEquals(0, personAdapter.binarySearchInsert(personOne));

        personAdapter.addToVisitors(0, personOne);
        Person personTwo = new Person();
        personTwo.setArriveTime(3);
        personTwo.setLeaveTime(4);
        assertEquals(1, personAdapter.binarySearchInsert(personTwo));

        personAdapter.addToVisitors(0, personTwo);
        Person personThree = new Person();
        personThree.setArriveTime(5);
        personThree.setLeaveTime(6);
        assertEquals(2, personAdapter.binarySearchInsert(personThree));
    }

    @Test
    public void binarySearchInsertThreeVisitorsMixed() {
        Person personOne = new Person();
        personOne.setArriveTime(1);
        personOne.setLeaveTime(2);
        assertEquals(0, personAdapter.binarySearchInsert(personOne));

        personAdapter.addToVisitors(0, personOne);
        Person personTwo = new Person();
        personTwo.setArriveTime(5);
        personTwo.setLeaveTime(6);
        assertEquals(1, personAdapter.binarySearchInsert(personTwo));

        personAdapter.addToVisitors(1, personTwo);
        Person personThree = new Person();
        personThree.setArriveTime(3);
        personThree.setLeaveTime(4);
        assertEquals(1, personAdapter.binarySearchInsert(personThree));
    }

    @Test
    public void binarySearchInsertThreeVisitorsDecrease() {
        Person personOne = new Person();
        personOne.setArriveTime(5);
        personOne.setLeaveTime(6);
        assertEquals(0, personAdapter.binarySearchInsert(personOne));

        personAdapter.addToVisitors(0, personOne);
        Person personTwo = new Person();
        personTwo.setArriveTime(3);
        personTwo.setLeaveTime(4);
        assertEquals(0, personAdapter.binarySearchInsert(personTwo));

        personAdapter.addToVisitors(0, personTwo);
        Person personThree = new Person();
        personThree.setArriveTime(1);
        personThree.setLeaveTime(2);
        assertEquals(0, personAdapter.binarySearchInsert(personThree));
    }

    @Test
    public void binarySearchInsertThreeVisitorsDecreaseMixed() {
        Person personOne = new Person();
        personOne.setArriveTime(5);
        personOne.setLeaveTime(6);
        assertEquals(0, personAdapter.binarySearchInsert(personOne));

        personAdapter.addToVisitors(0, personOne);
        Person personTwo = new Person();
        personTwo.setArriveTime(1);
        personTwo.setLeaveTime(2);
        assertEquals(0, personAdapter.binarySearchInsert(personTwo));

        personAdapter.addToVisitors(0, personTwo);
        Person personThree = new Person();
        personThree.setArriveTime(3);
        personThree.setLeaveTime(4);
        assertEquals(1, personAdapter.binarySearchInsert(personThree));
    }

    @Test
    public void binarySearchInsertThreeVisitorsEqualMixed() {
        Person personOne = new Person();
        personOne.setArriveTime(1);
        personOne.setLeaveTime(2);
        assertEquals(0, personAdapter.binarySearchInsert(personOne));

        personAdapter.addToVisitors(0, personOne);
        Person personTwo = new Person();
        personTwo.setArriveTime(1);
        personTwo.setLeaveTime(3);
        assertEquals(1, personAdapter.binarySearchInsert(personTwo));

        personAdapter.addToVisitors(1, personTwo);
        Person personThree = new Person();
        personThree.setArriveTime(4);
        personThree.setLeaveTime(5);
        assertEquals(2, personAdapter.binarySearchInsert(personThree));
    }

    @Test
    public void binarySearchInsertThreeVisitorsEqualMixedLess() {
        Person personOne = new Person();
        personOne.setArriveTime(1);
        personOne.setLeaveTime(3);
        assertEquals(0, personAdapter.binarySearchInsert(personOne));

        personAdapter.addToVisitors(0, personOne);
        Person personTwo = new Person();
        personTwo.setArriveTime(3);
        personTwo.setLeaveTime(4);
        assertEquals(1, personAdapter.binarySearchInsert(personTwo));

        personAdapter.addToVisitors(1, personTwo);
        Person personThree = new Person();
        personThree.setArriveTime(1);
        personThree.setLeaveTime(2);
        assertEquals(0, personAdapter.binarySearchInsert(personThree));
    }
}