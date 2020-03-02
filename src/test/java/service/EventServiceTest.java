package service;

import dao.Database;
import dao.EventDao;
import databaseAccessException.DataAccessException;
import model.Event;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.RegisterRequest;
import response.AllEventResponse;
import response.AllPersonResponse;
import response.SingleEventResponse;
import response.SinglePersonResponse;
import sun.jvm.hotspot.asm.Register;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

class EventServiceTest {
    private Database db;

    private EventDao eventDao;

    private Event testEvent;
    private Event testEvent2;

    String authToken;
    @BeforeEach
    void setUp() throws DataAccessException {
        testEvent = new Event("Sheila_Family_Map", "sheila", "Sheila_Parker",
                40.7500f, -110.1167f, "United States", "Salt Lake City", "started family map", 2016);
        testEvent2 = new Event(                "I_hate_formatting", "sheila",                "Patrick_Spencer"
                ,40.2338f,-111.6585f,"United States","Provo", "fixed this thing", 2017);

        db = new Database();
        Connection connection = db.openConnection();
        eventDao = new EventDao(connection);
        db.clearTables();
        eventDao.insertEvent(testEvent);
        eventDao.insertEvent(testEvent2);
        db.closeConnection(true);
        User compareUser = new User("sheila", "jj",
                "345345345", "cty26byu.edu", "JJ",
                "Yu", "Male");
        RegisterRequest r = new RegisterRequest(compareUser.getUserName(), compareUser.getPassword(), compareUser.getEmail(),
                compareUser.getFirstName(), compareUser.getLastName(), compareUser.getGender());
        RegisterService registerService = new RegisterService();
        authToken = registerService.register(r).getAuthToken();

    }


    @AfterEach
    void tearDown() throws DataAccessException {
        db.openConnection();
        db.clearTables();
        db.closeConnection(true);
    }

    @Test
    public void findEventByIDPass() {
        EventService eventTest = new EventService();

        SingleEventResponse singleEventResponse =
                eventTest.readSingleEvent(testEvent.getEventID(), authToken);

        assertTrue(singleEventResponse.isSuccess());
        assertEquals(singleEventResponse.getAssociatedUsername(), testEvent.getAssociatedUsername());
        assertEquals(singleEventResponse.getEventID(), testEvent.getEventID());
        assertEquals(singleEventResponse.getPersonID(), testEvent.getPersonID());
        assertEquals(singleEventResponse.getLatitude(), testEvent.getLatitude());
        assertEquals(singleEventResponse.getLongitude(), testEvent.getLongitude());
        assertEquals(singleEventResponse.getCountry(), testEvent.getCountry());
        assertEquals(singleEventResponse.getCity(), testEvent.getCity());
        assertEquals(singleEventResponse.getEventType(), testEvent.getEventType());
        assertEquals(singleEventResponse.getYear(), testEvent.getYear());
    }

    @Test
    public void findEventByIDfFail() {
        EventService eventTest = new EventService();

        SingleEventResponse singleEventResponse =
                eventTest.readSingleEvent("111", authToken);

        assertFalse(singleEventResponse.isSuccess());
        assertNotNull(singleEventResponse.getMessage());


    }

    @Test
    public void findAllEventPass() {
        EventService eventTest = new EventService();
        AllEventResponse allEventResponse = eventTest.readAllEvent(authToken);

        assertTrue(allEventResponse.isSuccess());
        assertNotNull(allEventResponse.getData());

    }

    @Test
    public void findAllMemeberFail() {
        EventService eventTest = new EventService();
        AllEventResponse allEventResponse = eventTest.readAllEvent("srte");

        assertFalse(allEventResponse.isSuccess());
        assertEquals(allEventResponse.getMessage(), "Error: Invalid auth token");
    }
}