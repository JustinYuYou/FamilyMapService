package dao;

import databaseAccessException.DataAccessException;
import model.Person;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

class PersonDaoTest {
    private Database db;
    private Person comparePerson;
    private Person comparePerson2;

    @BeforeEach
    public void setUp() throws Exception{
        db = new Database();
        comparePerson = new Person("123", "JustinYu117", "Justin",
                "Yu", "Male", null, null, null);
        comparePerson2 = new Person("1234", "jj2324", "Austin",
                "Ken", "Male", "345", "2345346", null);
    }

    @AfterEach
    public void tearDown() throws Exception {
        db.openConnection();
        db.clearTables();
        db.closeConnection(true);
    }

    @Test
    public void findPass() throws Exception{
        Person testPerson = null;
        try {
            Connection conn = db.openConnection();

            PersonDao personDao = new PersonDao(conn);
            personDao.insertPerson(comparePerson);

            testPerson = personDao.findPerson(comparePerson.getPersonID());
            db.closeConnection(true);

        } catch (DataAccessException e){
            db.closeConnection(false);
        }

        assertNotNull(testPerson);

        assertEquals(testPerson.getPersonID(), comparePerson.getPersonID());
        assertEquals(testPerson.getAssociatedUsername(), comparePerson.getAssociatedUsername());
        assertEquals(testPerson.getFirstName(), comparePerson.getFirstName());
        assertEquals(testPerson.getLastName(), comparePerson.getLastName());
        assertEquals(testPerson.getGender(), comparePerson.getGender());
        assertEquals(testPerson.getFatherID(), comparePerson.getFatherID());
        assertEquals(testPerson.getMotherID(), comparePerson.getMotherID());
        assertEquals(testPerson.getSpouseID(), comparePerson.getSpouseID());
    }

    @Test
    public void findFail() throws Exception{
        Person testPerson = null;

        try {
            Connection conn = db.openConnection();
            PersonDao personDao = new PersonDao(conn);

            personDao.insertPerson(comparePerson);
            testPerson = personDao.findPerson("1111");

            db.closeConnection(true);

        } catch (DataAccessException e) {
            db.closeConnection(false);
        }

        assertNull(testPerson);
    }

    @Test
    public void insertPerson() throws Exception {

        Person testPerson = null;

        try {
            Connection conn = db.openConnection();
            PersonDao personDao = new PersonDao(conn);

            personDao.insertPerson(comparePerson);

            testPerson = personDao.findPerson(comparePerson.getPersonID());
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }

        assertNotNull(testPerson);

        assertEquals(comparePerson, testPerson);
    }

    @Test
    public void insertFail() throws Exception {
        boolean didItWork = true;

        try {
            Connection conn = db.openConnection();
            PersonDao personDao = new PersonDao(conn);

            personDao.insertPerson(comparePerson);

            personDao.insertPerson(comparePerson);
            db.closeConnection(true);
        } catch (DataAccessException e) {

            db.closeConnection(false);
            didItWork = false;
        }

        assertFalse(didItWork);

        Person testPerson = comparePerson;
        try {
            Connection conn = db.openConnection();
            PersonDao personDao = new PersonDao(conn);

            testPerson = personDao.findPerson(comparePerson.getPersonID());
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }

        //Now make sure that testPerson is indeed null
        assertNull(testPerson);

        //Test the user has the same username
        boolean work = true;
        try {
            Connection conn = db.openConnection();
            PersonDao personDao = new PersonDao(conn);
            personDao.insertPerson(comparePerson);

            comparePerson2.setPersonID(comparePerson.getPersonID());
            personDao.insertPerson(comparePerson);

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

        Person testPerson = null;
        try {
            conn = db.openConnection();
            PersonDao personDao = new PersonDao(conn);
            personDao.insertPerson(comparePerson);
            personDao.insertPerson(comparePerson2);

            personDao.deleteAllPersons();

            testPerson = personDao.findPerson(comparePerson2.getPersonID());
            db.closeConnection(true);

        } catch (DataAccessException e) {
            db.closeConnection(false);
        }

        assertNull(testPerson);
    }

}