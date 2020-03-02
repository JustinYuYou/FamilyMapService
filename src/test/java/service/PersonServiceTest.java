package service;

import dao.Database;
import dao.PersonDao;
import databaseAccessException.DataAccessException;
import model.AuthToken;
import model.Person;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.RegisterRequest;
import response.AllPersonResponse;
import response.RegisterResponse;
import response.SinglePersonResponse;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

class PersonServiceTest {
    private Database db;
    private PersonDao personDao;

    private Person testPerson;
    private Person testPerson2;
    private User user;

    String authToken;
    @BeforeEach
    void setUp() throws DataAccessException {

        testPerson = new Person("Sheila_Parker", "sheila"
                , "Sheila", "Parker", "f", "Patrick_Spencer",
                "Im_really_good_at_names", null);

        testPerson2 = new Person("Patrick_Spencer", "sheila",
                "Patrick", "Spencer", "m",
                null, null, "Im_really_good_at_names");
        db = new Database();
        Connection connection = db.openConnection();
        db.clearTables();
        personDao = new PersonDao(connection);
        personDao.insertPerson(testPerson);
        personDao.insertPerson(testPerson2);
        db.closeConnection(true);

        user = new User("sheila", "235we",
                "ewof4wef", "Parker", "Patrick", "james", "f");
        RegisterService registerService = new RegisterService();
        RegisterRequest registerRequest = new RegisterRequest(user.getUserName(), user.getPassword(),
                user.getEmail(), user.getFirstName(), user.getLastName(), user.getGender());
        authToken = registerService.register(registerRequest).getAuthToken();


    }

    @AfterEach
    void tearDown() throws DataAccessException {
        db.openConnection();
        db.clearTables();
        db.closeConnection(true);
    }

    @Test
    public void findPersonByIDPass() {
        PersonService personTest = new PersonService();

        SinglePersonResponse singlePersonResponse =
                personTest.readSinglePerson(testPerson.getPersonID(), authToken);

        assertTrue(singlePersonResponse.isSuccess());
        assertEquals(singlePersonResponse.getAssociatedUsername(), testPerson.getAssociatedUsername());
        assertEquals(singlePersonResponse.getPersonID(), testPerson.getPersonID());
        assertEquals(singlePersonResponse.getFirstName(), testPerson.getFirstName());
        assertEquals(singlePersonResponse.getLastName(), testPerson.getLastName());
        assertEquals(singlePersonResponse.getFatherID(), testPerson.getFatherID());
        assertEquals(singlePersonResponse.getMotherID(), testPerson.getMotherID());
        assertEquals(singlePersonResponse.getGender(), testPerson.getGender());
    }

    @Test
    public void findPersonByIDfFail() {
        PersonService personTest = new PersonService();

        SinglePersonResponse singlePersonResponse =
                personTest.readSinglePerson("111", authToken);

        assertFalse(singlePersonResponse.isSuccess());
        assertNotNull(singlePersonResponse.getMessage());


    }

    @Test
    public void findAllMemberPass() {
        PersonService personTest = new PersonService();
        AllPersonResponse allPersonResponse = personTest.readAllPerson(authToken);

        assertTrue(allPersonResponse.isSuccess());
        assertNotNull(allPersonResponse.getData());

    }

    @Test
    public void findAllMemeberFail() {
        PersonService personTest = new PersonService();
        AllPersonResponse allPersonResponse = personTest.readAllPerson("srte");

        assertFalse(allPersonResponse.isSuccess());
        assertEquals(allPersonResponse.getMessage(), "Error: Invalid auth token");
    }
}