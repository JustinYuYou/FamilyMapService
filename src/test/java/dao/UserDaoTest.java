package dao;

import databaseAccessException.DataAccessException;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

class UserDaoTest {
    private Database db;
    private User compareUser;
    private User compareUser2;

    @BeforeEach
    void setUp() throws Exception{
        //here we can set up any classes or variables we will need for the rest of our tests
        //lets create a new database
        db = new Database();
        compareUser = new User("12345", "jj",
                "345345345", "cty26byu.edu", "JJ",
                "Yu", "Male");
        compareUser2 = new User("12334", "jj",
                "345345", "sdfdsf@sdfuo", "sdf",
                "sdfsdf", "Female");
    }

    @AfterEach
    void tearDown() throws Exception {
        //here we can get rid of anything from our tests we don't want to affect the rest of our program
        //lets clear the tables so that any data we entered for testing doesn't linger in our files
        db.openConnection();
        db.clearTables();
        db.closeConnection(true);
    }




    @Test
    public void findPass() throws Exception{
        User testUser = null;
        try {
            Connection conn = db.openConnection();
            UserDao userDao = new UserDao(conn);
            userDao.insertUser(compareUser);
            testUser = userDao.findUser(compareUser.getUserName());
            db.closeConnection(true);
        } catch (DataAccessException e){
            db.closeConnection(false);
        }

        assertNotNull(testUser);

        assertEquals(testUser.getUserName(), compareUser.getUserName());
        assertEquals(testUser.getPersonID(), compareUser.getPersonID());
        assertEquals(testUser.getPassword(), compareUser.getPassword());
        assertEquals(testUser.getEmail(), compareUser.getEmail());
        assertEquals(testUser.getFirstName(), compareUser.getFirstName());
        assertEquals(testUser.getLastName(), compareUser.getLastName());
        assertEquals(testUser.getGender(), compareUser.getGender());

    }
    @Test
    public void findFail() throws Exception {
        User compareTest = null;

        try {
            Connection conn = db.openConnection();
            UserDao userDao = new UserDao(conn);

            userDao.insertUser(compareUser);
            compareTest = userDao.findUser("1111");

            db.closeConnection(true);

        } catch (DataAccessException e) {
            db.closeConnection(false);
        }

        assertNull(compareTest);

    }

    @Test
    public void insertPass() throws Exception{
        //We want to make sure insert works
        //First lets create an User that we'll set to null. We'll use this to make sure what we put
        //in the database is actually there.
        User compareTest = null;

        try {
            Connection conn = db.openConnection();
            UserDao userDao = new UserDao(conn);

            //While insert returns a bool we can't use that to verify that our function actually worked
            //only that it ran without causing an error
            userDao.insertUser(compareUser);
            //So lets use a find method to get the event that we just put in back out
            compareTest = userDao.findUser(compareUser.getUserName());
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }

        //First lets see if our find found anything at all. If it did then we know that if nothing
        //else something was put into our database, since we cleared it in the beginning
        assertNotNull(compareTest);
        //Now lets make sure that what we put in is exactly the same as what we got out. If this
        //passes then we know that our insert did put something in, and that it didn't change the
        //data in any way
        assertEquals(compareUser, compareTest);
    }
    @Test
    public void insertFail() throws Exception {
        boolean didItWork = true;

        try {
            Connection conn = db.openConnection();
            UserDao userDao = new UserDao(conn);
            //if we call the method the first time it will insert it successfully
            userDao.insertUser(compareUser);
            //but our sql table is set up so that "userName" must be unique. So trying to insert it
            //again will cause the method to throw an exception
            userDao.insertUser(compareUser);
            db.closeConnection(true);
        } catch (DataAccessException e) {
            //If we catch an exception we will end up in here, where we can change our boolean to
            //false to show that our function failed to perform correctly
            db.closeConnection(false);
            didItWork = false;
        }

        assertFalse(didItWork);

        User compareTest = compareUser;
        try {
            Connection conn = db.openConnection();
            UserDao userDao = new UserDao(conn);
            //and then get something back from our find. If the event is not in the database we
            //should have just changed our compareTest to a null object
            compareTest = userDao.findUser(compareUser.getUserName());
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }

        //Now make sure that compareTest is indeed null
        assertNull(compareTest);

        //Test the user has the same username
        boolean work = true;
        try {
            Connection conn = db.openConnection();
            UserDao userDao = new UserDao(conn);
            userDao.insertUser(compareUser);

            compareUser2.setUserName(compareUser.getUserName());

            userDao.insertUser(compareUser);
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
            work = false;
        }

        assertFalse(work);
    }

    @Test
    void deletePass() throws Exception{
        Connection conn = null;

        User compareTest = null;
        try {
            conn = db.openConnection();
            UserDao userDao = new UserDao(conn);
            userDao.insertUser(compareUser);
            userDao.insertUser(compareUser2);

            userDao.deleteAllUsers();

            compareTest = userDao.findUser(compareUser.getUserName());
            db.closeConnection(true);

        } catch (DataAccessException e) {
            db.closeConnection(false);
        }

        assertNull(compareTest);

    }
}