package service;

import dao.*;
import databaseAccessException.DataAccessException;
import model.AuthToken;
import model.Event;
import model.Person;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

class ClearServiceTest {
    private Database db;

    private AuthToken compareAuthToken;
    private AuthToken compareAuthToken2;

    private User compareUser;
    private User compareUser2;
    private User user;

    private Person comparePerson;
    private Person comparePerson2;

    private Event compareEvent;
    private Event compareEvent2;

    @BeforeEach
    void setUp() {
        db = new Database();

        compareAuthToken = new AuthToken("11111", "JustinYu117");
        compareAuthToken2 = new AuthToken("11112", "jj2324");
        compareUser = new User("12345", "jj",
                "345345345", "cty26byu.edu", "JJ",
                "Yu", "Male");
        compareUser2 = new User("12334", "jj",
                "345345", "sdfdsf@sdfuo", "sdf",
                "sdfsdf", "Female");
        user = new User("sheila", "235we",
                "ewof4wef", "Parker", "Patrick", "james", "f");
        comparePerson = new Person("123", "JustinYu117", "Justin",
                "Yu", "Male", null, null, null);
        comparePerson2 = new Person("1234", "jj2324", "Austin",
                "Ken", "Male", "345", "2345346", null);

        compareEvent = new Event("1234444", "JustinYu117", "123",
                345345345,31231, "Taiwan", "Taoyuan", "Birth", 1997);
        compareEvent2 = new Event("111111", "jj2324", "4444",
                3453453, 346345, "Japan", "Osaka", "Marriage", 1996);
    }

    @AfterEach
    void tearDown() throws Exception {
        db.openConnection();
        db.clearTables();
        db.closeConnection(true);
    }

    @Test
    public void clearServicePass() throws Exception {
        Connection conn = null;
        ClearService clearService = new ClearService();

        AuthToken a;
        User u;
        Person p;
        Event e;


        try {
            conn = db.openConnection();

            AuthTokenDao authTokenDao = new AuthTokenDao(conn);
            authTokenDao.insertAuthToken(compareAuthToken);

            UserDao userDao = new UserDao(conn);
            userDao.insertUser(compareUser);
            userDao.insertUser(compareUser2);

            PersonDao personDao = new PersonDao(conn);
            personDao.insertPerson(comparePerson);
            personDao.insertPerson(comparePerson2);

            EventDao eventDao = new EventDao(conn);
            eventDao.insertEvent(compareEvent);
            eventDao.insertEvent(compareEvent2);

            db.closeConnection(true);

        } catch (DataAccessException ex) {
            db.closeConnection(false);
        }

        clearService.clear();

        conn = db.openConnection();

        AuthTokenDao authTokenDao = new AuthTokenDao(conn);
        a = authTokenDao.findAuthToken(compareAuthToken.getAuthToken());

        UserDao userDao = new UserDao(conn);
        u = userDao.findUser(compareUser.getUserName());

        PersonDao personDao = new PersonDao(conn);
        p = personDao.findPerson(comparePerson2.getPersonID());

        EventDao eventDao = new EventDao(conn);
        e = eventDao.findEvent(compareEvent.getEventID());

        db.closeConnection(true);

        assertEquals(a , null);
        assertEquals(u , null);
        assertEquals(p , null);
        assertEquals(e , null);


    }
}