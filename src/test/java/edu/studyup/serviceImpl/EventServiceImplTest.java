package edu.studyup.serviceImpl;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Calendar;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import edu.studyup.entity.Event;
import edu.studyup.entity.Location;
import edu.studyup.entity.Student;
import edu.studyup.util.DataStorage;
import edu.studyup.util.StudyUpException;


class EventServiceImplTest {

	EventServiceImpl eventServiceImpl;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		eventServiceImpl = new EventServiceImpl();
		//Create Student
		Student student = new Student();
		student.setFirstName("John");
		student.setLastName("Doe");
		student.setEmail("JohnDoe@email.com");
		student.setId(1);
		
		//Create Event1
		Event event = new Event();
		event.setEventID(1);
		event.setDate(new Date());
		event.setName("Event 1");
		Location location = new Location(-122, 37);
		event.setLocation(location);
		List<Student> eventStudents = new ArrayList<>();
		eventStudents.add(student);
		event.setStudents(eventStudents);
		
		DataStorage.eventData.put(event.getEventID(), event);
		
	}

	@AfterEach
	void tearDown() throws Exception {
		DataStorage.eventData.clear();
	}

	@Test
	void testUpdateEventName_GoodCase() throws StudyUpException {
		int eventID = 1;
		eventServiceImpl.updateEventName(eventID, "Renamed Event 1");
		assertEquals("Renamed Event 1", DataStorage.eventData.get(eventID).getName());
	}
	
	@Test
	void testUpdateEvent_WrongEventID_badCase() {
		int eventID = 3;
		Assertions.assertThrows(StudyUpException.class, () -> {
			eventServiceImpl.updateEventName(eventID, "Renamed Event 3");
		  });
	}
	
	@Test
	void testUpdateGetPast() {	
	
		//Create Event2
		Event event = new Event();
		event.setEventID(10);
		event.setDate(new Date(324242354));  //Jan 4, 1970
		event.setName("Event 10");
	
		DataStorage.eventData.put(event.getEventID(), event);
		//2/1/2008
		Event event11 = new Event();
		event11.setEventID(11);
		event11.setDate(new Date(324242354));  //Jan 4,1970
		event11.setName("Event 11");
		DataStorage.eventData.put(event.getEventID(), event11);
		
		List<Event> pastEvents = eventServiceImpl.getPastEvents();
		
		for (int i = 0; i < pastEvents.size(); i++) {
			Event ithEvent = pastEvents.get(i);
			assertTrue(ithEvent.getDate().before(new Date()));
		}
	} //should pass
	
	@Test
	void testaddStudentToEvents_nullEvent() {
		Assertions.assertThrows(StudyUpException.class, () ->  {
			eventServiceImpl.addStudentToEvent(null, -1);
		});
	}
	
	@Test
	void testdeleteEvent_deletingNonExitantEvent() {
		assertTrue(eventServiceImpl.deleteEvent(-1) == null);
	}
	
	/* ******************** HW starts here ********************* */
	
	@Test
	void testUpdateEventName_greaterThanTwenty() throws StudyUpException {
		int eventID = 1;
		String name = "My length is greater than twenty.";
		Assertions.assertThrows(StudyUpException.class, () -> {
			eventServiceImpl.updateEventName(eventID, name);
		  });
	} //this should throw an exception
	
	@Test
	void testUpdatEventName_lessThanTwenty() {
		int eventID = 2;
		String name = "My length < 20";
		Assertions.assertThrows(StudyUpException.class, () -> {
			eventServiceImpl.updateEventName(eventID, name);
		  });
	}//should pass just fine
	
	@Test
	void testUpdateEventName_equalTwenty() throws StudyUpException {
		int eventID = 3;
		String name = "My length is twenty.";
		eventServiceImpl.updateEventName(eventID, name);
		assertEquals(name, DataStorage.eventData.get(eventID).getName());
	}//testing id with length 20, it does not get accepted
	//should not throw exception but does
	
	@Test
	void testgetActiveEvents() {
        Date currentDate = new Date();

        // convert date to calendar
        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);

        // manipulate date
        c.add(Calendar.YEAR, 1);
        c.add(Calendar.MONTH, 1);
        c.add(Calendar.DATE, 1); //same with c.add(Calendar.DAY_OF_MONTH, 1);
        c.add(Calendar.HOUR, 1);
        c.add(Calendar.MINUTE, 1);
        c.add(Calendar.SECOND, 1);

        // convert calendar to date
        Date currentDatePlusOne = c.getTime();
        
        //the above code was references from:
        //https://www.mkyong.com/java/java-how-to-add-days-to-current-date/
		
		//Create Student2
		Student student2 = new Student();
		student2.setFirstName("Jane");
		student2.setLastName("Doe");
		student2.setEmail("JaneDoe@email.com");
		student2.setId(2);
				
		//Create Event2
		Event event2 = new Event();
		event2.setEventID(2);
		event2.setDate(currentDatePlusOne); // January 1st 2020
		event2.setName("Event 2");
		Location location2 = new Location(-123, 35);
		event2.setLocation(location2);
		List<Student> eventStudents2 = new ArrayList<>();
		eventStudents2.add(student2);
		event2.setStudents(eventStudents2);
				
		DataStorage.eventData.put(event2.getEventID(), event2);
		
		List<Event> activeEvents = eventServiceImpl.getActiveEvents();
		
		
		for(int i = 0; i < activeEvents.size(); i++) {
			Event ithEvent = activeEvents.get(i);
			assertTrue(ithEvent.getDate().after(new Date()));
		}
	}//checks if all "active" events are actually in the future
	//throws exception if they are not
	
	@Test
	void testsetStudents_MoreThanTwo() {
		//Create Student
		Student student1 = new Student();
		Student student2 = new Student();
		Student student3 = new Student();
		//Event is initially created with > 2 in eventStudents
		//Create Event2
		Event event = new Event();
		event.setEventID(3);
		event.setDate(new Date()); 
		event.setName("Event 3");
		Location location = new Location(0, 0);
		event.setLocation(location);
		List<Student> eventStudents = new ArrayList<>();
		eventStudents.add(student1);
		eventStudents.add(student2);
		eventStudents.add(student3);
		event.setStudents(eventStudents);		
		DataStorage.eventData.put(event.getEventID(), event);
		
		assertTrue(DataStorage.eventData.get(event.getEventID()).getStudents().size() <= 2);
	} //should fail
	
	@Test
	void testsetStudents_EqualOne() {
		//Create Student
		Student student1 = new Student();

		//Event is initially created with < 2 in eventStudents
		//Create Event 4
		Event event = new Event();
		event.setEventID(4);
		event.setDate(new Date()); 
		event.setName("Event 4");
		Location location = new Location(1, 1);
		event.setLocation(location);
		List<Student> eventStudents = new ArrayList<>();
		eventStudents.add(student1);

		event.setStudents(eventStudents);		
		DataStorage.eventData.put(event.getEventID(), event);
		
		assertTrue(DataStorage.eventData.get(event.getEventID()).getStudents().size() < 2);
	} //should pass
	
	@Test
	void testsetStudents_EqualTwo() {
		//Create Student
		Student student1 = new Student();
		Student student2 = new Student();
		
		//Event is initially created with < 2 in eventStudents
		//Create Event 5
		Event event = new Event();
		event.setEventID(5);
		event.setDate(new Date()); 
		event.setName("Event 5");
		Location location = new Location(2, 2);
		event.setLocation(location);
		List<Student> eventStudents = new ArrayList<>();
		eventStudents.add(student1);
		eventStudents.add(student2);

		event.setStudents(eventStudents);		
		DataStorage.eventData.put(event.getEventID(), event);
		
		assertTrue(DataStorage.eventData.get(event.getEventID()).getStudents().size() == 2);
	} //should pass
	
	@Test
	void testsetStudents_NoStudents() {
		//Create Event 6
		Event event = new Event();
		event.setEventID(6);
		event.setDate(new Date()); 
		event.setName("Event 6");
		Location location = new Location(5, 5);
		event.setLocation(location);
		List<Student> eventStudents = new ArrayList<>();

		event.setStudents(eventStudents);
		DataStorage.eventData.put(event.getEventID(), event);
		
		assertTrue(DataStorage.eventData.get(event.getEventID()).getStudents().size() <= 2);
	} //should pass
	
	@Test
	void testaddStudentToEvent_MoreThanTwo() throws StudyUpException{
		//Create Student1 for Event 7
		Student student = new Student();
		//Create Event7
		Event event = new Event();
		event.setEventID(7);
		event.setDate(new Date(324242354));  //Jan 4, 1970
		event.setName("Event 7");
		Location location = new Location(0, 0);
		event.setLocation(location);
		eventStudents.add(student);
		DataStorage.eventData.put(event.getEventID(), event);
		//Create Student2 for Event 7
		Student student2 = new Student();
		eventServiceImpl.addStudentToEvent(student2, event.getEventID());
		assertTrue(DataStorage.eventData.get(event.getEventID()).getStudents().size() <= 2);
		
		//Create Student3,4,5 for Event 7
		Student student3 = new Student();
		Student student4 = new Student();
		Student student5 = new Student();
		
		eventServiceImpl.addStudentToEvent(student3, event.getEventID());
		eventServiceImpl.addStudentToEvent(student4, event.getEventID());
		eventServiceImpl.addStudentToEvent(student5, event.getEventID());
		assertTrue(DataStorage.eventData.get(event.getEventID()).getStudents().size() <= 2);
		
	}//should throw exception upon adding too students
	
	@Test
	void testaddStudentToEvent_EqualTwo() throws StudyUpException {
		Student student = new Student();
		Student student2 = new Student();
		
		//Create Event8
		Event event = new Event();
		event.setEventID(8);
		event.setDate(new Date());  //Jan 4, 1970
		event.setName("Event 8");
		Location location = new Location(8, 8);
		event.setLocation(location);
		DataStorage.eventData.put(event.getEventID(), event);
		
		eventServiceImpl.addStudentToEvent(student, event.getEventID());
		eventServiceImpl.addStudentToEvent(student2, event.getEventID());
		
		assertTrue(DataStorage.eventData.get(event.getEventID()).getStudents().size() <= 2);
		
	} //should pass
	
	@Test
	void testaddStudentToEvent_EqualOne() throws StudyUpException {
		Student student = new Student();
		
		//Create Event8
		Event event = new Event();
		event.setEventID(9);
		event.setDate(new Date());  //Jan 4, 1970
		event.setName("Event 9");
		Location location = new Location(9, 9);
		event.setLocation(location);
		DataStorage.eventData.put(event.getEventID(), event);
		
		eventServiceImpl.addStudentToEvent(student, event.getEventID());
		
		assertTrue(DataStorage.eventData.get(event.getEventID()).getStudents().size() <= 2);
	} //should pass

	@Test
	void testaddStudentToEvent_NoStudent() throws StudyUpException {
		//Create Event12
		Event event = new Event();
		event.setEventID(12);
		event.setDate(new Date());  //Jan 4, 1970
		event.setName("Event 12");
		Location location = new Location(12, 12);
		event.setLocation(location);
		
		DataStorage.eventData.put(event.getEventID(), event);
		event = eventServiceImpl.addStudentToEvent(null, 12); 
		//adds a student but that student holds null
		assertTrue(1 == event.getStudents().size());
	}
}
