package service;

import dao.Database;
import dao.EventDao;
import dao.PersonDao;
import dao.UserDao;
import databaseAccessException.DataAccessException;
import model.AuthToken;
import model.Event;
import model.Person;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.LoadRequest;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LoadServiceTest {

    private Database db;

    private UserDao userDao;
    private PersonDao personDao;
    private EventDao eventDao;

    private User testUser;

    private Person testPerson;
    private Person testPerson2;

    private Event testEvent;
    private Event testEvent2;

    @BeforeEach
    void setUp() throws Exception {

        testUser = new User("sheila", "Sheila_Parker",
                "parker", "sheila@parker.com",
                "Sheila", "Parker",
                "f");
        testPerson = new Person("Sheila_Parker", "sheila"
                , "Sheila", "Parker", "f", "Patrick_Spencer",
                "Im_really_good_at_names", null);

        testPerson2 = new Person("Patrick_Spencer", "sheila",
                "Patrick", "Spencer", "m",
                null, null, "Im_really_good_at_names");
        testEvent = new Event("Sheila_Family_Map", "sheila", "Sheila_Parker",
                40.7500f, -110.1167f, "United States", "Salt Lake City", "started family map", 2016);
        testEvent2 = new Event(                "I_hate_formatting", "sheila",                "Patrick_Spencer"
                ,40.2338f,-111.6585f,"United States","Provo", "fixed this thing", 2017);

        db = new Database();
        db.openConnection();
        db.clearTables();
        db.closeConnection(true);

    }

    @AfterEach
    void tearDown() throws Exception {
        db.openConnection();
        db.clearTables();
        db.closeConnection(true);
    }

    @Test
    public void loadPass() throws Exception {

        LoadService loadTest = new LoadService();

        List<User> users = new ArrayList<>();
        users.add(testUser);

        List<Person> persons = new ArrayList<>();
        persons.add(testPerson);
        persons.add(testPerson2);

        List<Event> events = new ArrayList<>();
        events.add(testEvent);
        events.add(testEvent2);

        LoadRequest r = new LoadRequest(users, persons, events);
        loadTest.load(r);

        try {
            Connection conn = db.openConnection();
            personDao = new PersonDao(conn);
            eventDao = new EventDao(conn);
            userDao = new UserDao(conn);

            System.out.println(userDao.findUser(testUser.getUserName()));
            assertEquals(testUser, userDao.findUser(testUser.getUserName()));
            assertEquals(testPerson, personDao.findPerson(testPerson.getPersonID()));
            assertEquals(testPerson2, personDao.findPerson(testPerson2.getPersonID()));
            assertEquals(testEvent, eventDao.findEvent(testEvent.getEventID()));
            assertEquals(testEvent2, eventDao.findEvent(testEvent2.getEventID()));

            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
            e.printStackTrace();
        }


    }
}