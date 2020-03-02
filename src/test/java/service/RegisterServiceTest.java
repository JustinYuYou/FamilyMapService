package service;

import dao.Database;
import model.AuthToken;
import model.Event;
import model.Person;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.RegisterRequest;
import response.RegisterResponse;

import javax.xml.crypto.Data;

import static org.junit.jupiter.api.Assertions.*;

class RegisterServiceTest {
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
    public void registerPass() throws Exception {
        RegisterService registerService = new RegisterService();
        RegisterRequest r  = new RegisterRequest(compareUser.getUserName(), compareUser.getPassword(), compareUser.getEmail(),
                compareUser.getFirstName(), compareUser.getLastName(), compareUser.getGender());
        RegisterResponse registerResponse = registerService.register(r);

        assertTrue(registerResponse.isSuccess());
        assertNotNull(registerResponse.getAuthToken());
        assertNotNull(registerResponse.getUserName());
        assertNotNull(registerResponse.getPersonID());
    }
    @Test
    public void registerFail() throws Exception {
        RegisterService registerService = new RegisterService();
        RegisterRequest r  = new RegisterRequest(compareUser.getUserName(), compareUser.getPassword(), compareUser.getEmail(),
                compareUser.getFirstName(), compareUser.getLastName(), compareUser.getGender());
        registerService.register(r);


        RegisterRequest r2  = new RegisterRequest(compareUser.getUserName(), compareUser.getPassword(), compareUser.getEmail(),
                compareUser.getFirstName(), compareUser.getLastName(), compareUser.getGender());
        RegisterResponse registerResponse = registerService.register(r2);


        assertFalse(registerResponse.isSuccess());
        assertNotNull(registerResponse.getMessage());

    }
}