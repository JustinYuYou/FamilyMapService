package service;

import dao.AuthTokenDao;
import dao.Database;
import dao.PersonDao;
import dao.UserDao;
import databaseAccessException.DataAccessException;
import model.AuthToken;
import model.Person;
import model.User;
import response.AllPersonResponse;
import response.SinglePersonResponse;
import util.GeneratePerson;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * URL Path: /person/[personID]
 * Example: /person/7255e93e
 * Description: Returns the single Person object with the specified ID.
 */
public class PersonService {
    public SinglePersonResponse readSinglePerson(String personID) {
        Database db = new Database();
        PersonDao personDao;
        Person person = null;

        try {
            personDao = new PersonDao(db.openConnection());
            person = personDao.findPerson(personID);
            db.closeConnection(true);
        } catch (DataAccessException e) {
           System.out.println(e);
            try {
                db.closeConnection(false);
            } catch (DataAccessException ex) {
                ex.printStackTrace();
            }
        }
        if (person != null) {
            return new SinglePersonResponse(person.getAssociatedUsername(), person.getPersonID(),
                    person.getFirstName(), person.getLastName(), person.getGender(), person.getFatherID(),
                    person.getMotherID(), person.getSpouseID(), true);
        } else {
            return new SinglePersonResponse("Invalid personID parameter", false);
        }
    }

    public AllPersonResponse readAllPerson(String authTokenString) {
        List<Person> allFamilyMembers = new ArrayList<>();
        Database db = new Database();
        AuthTokenDao authTokenDao;
        AuthToken authToken = null;
        UserDao userDao;
        User user = null;
        Connection connection;
        try {
            connection = db.openConnection();
            authTokenDao = new AuthTokenDao(connection);
            authToken = authTokenDao.findAuthToken(authTokenString);
            String username = authToken.getAssociatedUsername();

            userDao = new UserDao(connection);
            user = userDao.findUser(username);

        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
