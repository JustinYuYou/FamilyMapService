package service;

import dao.Database;
import dao.UserDao;
import databaseAccessException.DataAccessException;
import model.AuthToken;
import model.Event;
import model.Person;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.FillRequest;
import response.FillResponse;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class FillServiceTest {

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

        try {
            db.openConnection();
            db.clearTables();
            db.closeConnection(true);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }

    }

    @AfterEach
    void tearDown() throws Exception {
        db.openConnection();
        db.clearTables();
        db.closeConnection(true);
    }

    @Test
    public void fillPass() throws Exception {

        Connection conn = db.openConnection();
        UserDao userDao = new UserDao(conn);
        userDao.insertUser(user);
        db.closeConnection(true);


        FillService fillService = new FillService();
        FillRequest fillRequest = new FillRequest(user.getUserName(), 4);
        FillResponse fillResponse = fillService.fill(fillRequest);

        assertTrue(fillResponse.isSuccess());
        assertEquals(fillResponse.getMessage(), "Successfully added 31 persons and 91 events to the database.");


    }

    @Test
    public void fillFail() throws Exception {
        FillService fillService = new FillService();
        FillRequest fillRequest = new FillRequest(user.getUserName(), -1);
        FillResponse fillResponse = fillService.fill(fillRequest);

        assertFalse(fillResponse.isSuccess());
        assertNotNull(fillResponse.getMessage());
    }
}
