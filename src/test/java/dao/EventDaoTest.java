package dao;

import databaseAccessException.DataAccessException;
import model.Event;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

class EventDaoTest {

    private Database db;
    private Event compareEvent;
    private Event compareEvent2;

    @BeforeEach
    public void setUp() throws Exception{
        db = new Database();
        compareEvent = new Event("1234444", "JustinYu117", "123", 
                345345345,31231, "Taiwan", "Taoyuan", "Birth", 1997);
        compareEvent2 = new Event("111111", "jj2324", "4444",
                3453453, 346345, "Japan", "Osaka", "Marriage", 1996);
    }

    @AfterEach
    public void tearDown() throws Exception {
        db.openConnection();
        db.clearTables();
        db.closeConnection(true);
    }

    @Test
    public void findPass() throws Exception{
        Event testEvent = null;
        try {
            Connection conn = db.openConnection();

            EventDao eventDao = new EventDao(conn);
            eventDao.insertEvent(compareEvent);

            testEvent = eventDao.findEvent(compareEvent.getEventID());
            db.closeConnection(true);

        } catch (DataAccessException e){
            db.closeConnection(false);
        }

        assertNotNull(testEvent);

        assertEquals(testEvent.getEventID(), compareEvent.getEventID());
        assertEquals(testEvent.getAssociatedUsername(), compareEvent.getAssociatedUsername());
        assertEquals(testEvent.getPersonID(), compareEvent.getPersonID());
        assertEquals(testEvent.getLatitude(), compareEvent.getLatitude());
        assertEquals(testEvent.getLongitude(), compareEvent.getLongitude());
        assertEquals(testEvent.getCountry(), compareEvent.getCountry());
        assertEquals(testEvent.getCity(), compareEvent.getCity());
        assertEquals(testEvent.getEventType(), compareEvent.getEventType());
        assertEquals(testEvent.getYear(), compareEvent.getYear());
    }

    @Test
    public void findFail() throws Exception{
        Event testEvent = null;

        try {
            Connection conn = db.openConnection();
            EventDao eventDao = new EventDao(conn);

            eventDao.insertEvent(compareEvent);
            testEvent = eventDao.findEvent("1111");

            db.closeConnection(true);

        } catch (DataAccessException e) {
            db.closeConnection(false);
        }

        assertNull(testEvent);
    }

    @Test
    public void insertEvent() throws Exception {

        Event testEvent = null;

        try {
            Connection conn = db.openConnection();
            EventDao eventDao = new EventDao(conn);

            eventDao.insertEvent(compareEvent);

            testEvent = eventDao.findEvent(compareEvent.getEventID());
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }

        assertNotNull(testEvent);

        assertEquals(compareEvent, testEvent);
    }

    @Test
    public void insertFail() throws Exception {
        boolean didItWork = true;

        try {
            Connection conn = db.openConnection();
            EventDao eventDao = new EventDao(conn);

            eventDao.insertEvent(compareEvent);

            eventDao.insertEvent(compareEvent);
            db.closeConnection(true);
        } catch (DataAccessException e) {

            db.closeConnection(false);
            didItWork = false;
        }

        assertFalse(didItWork);

        Event testEvent = compareEvent;
        try {
            Connection conn = db.openConnection();
            EventDao eventDao = new EventDao(conn);

            testEvent = eventDao.findEvent(compareEvent.getEventID());
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }

        //Now make sure that testEvent is indeed null
        assertNull(testEvent);

        //Test the user has the same username
        boolean work = true;
        try {
            Connection conn = db.openConnection();
            EventDao eventDao = new EventDao(conn);
            eventDao.insertEvent(compareEvent);

            compareEvent2.setEventID(compareEvent.getEventID());
            eventDao.insertEvent(compareEvent);

            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
            work = false;
        }

        assertFalse(work);
    }

    @Test
    public void deletePass() throws Exception {
        Connection conn = null;

        Event testEvent = null;
        try {
            conn = db.openConnection();
            EventDao eventDao = new EventDao(conn);
            eventDao.insertEvent(compareEvent);
            eventDao.insertEvent(compareEvent2);

            eventDao.deleteAllEvents();

            testEvent = eventDao.findEvent(compareEvent2.getEventID());
            db.closeConnection(true);

        } catch (DataAccessException e) {
            db.closeConnection(false);
        }

        assertNull(testEvent);
    }

}