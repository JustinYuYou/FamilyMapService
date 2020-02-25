package service;

import dao.Database;
import dao.EventDao;
import dao.PersonDao;
import dao.UserDao;
import model.AuthToken;
import model.Event;
import model.Person;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoadServiceTest extends ServiceTestParent{

    Database db = new Database();

    private UserDao userDao;
    private PersonDao personDao;
    private EventDao eventDao;

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
    void setUp() throws Exception{
       super.clearingTableToTest();
    }

    @AfterEach
    void tearDown() throws Exception{
        db.openConnection();
        db.clearTables();
        db.closeConnection(true);
    }

    @Test
    public void loadPass() throws Exception {

    }
}