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
    public SinglePersonResponse readSinglePerson(String personID, String authTokenString) {
        Database db = new Database();
        PersonDao personDao;
        Person person = null;

        AuthTokenDao authTokenDao;
        AuthToken authToken;

        SinglePersonResponse singlePersonResponse = null;

        try {

            Connection connection = db.openConnection();

            authTokenDao = new AuthTokenDao(connection);
            authToken = authTokenDao.findAuthToken(authTokenString);
            boolean isCommit = true;

            if (authToken == null) {
                singlePersonResponse = new SinglePersonResponse("Error: the authToken is not right", false);
                isCommit = false;
            } else {
                personDao = new PersonDao(connection);
                person = personDao.findPerson(personID);

                if(person == null) {
                    singlePersonResponse = new SinglePersonResponse("Error: Invalid personID parameter", false);
                    isCommit = false;
                } else if(!authToken.getAssociatedUsername().equals(person.getAssociatedUsername())) {
                    singlePersonResponse = new SinglePersonResponse("Error: Invalid personID parameter", false);
                    isCommit = false;
                } else {
                    if (person != null) {
                        singlePersonResponse = new SinglePersonResponse(person.getAssociatedUsername(), person.getPersonID(),
                                person.getFirstName(), person.getLastName(), person.getGender(), person.getFatherID(),
                                person.getMotherID(), person.getSpouseID(), true);
                    } else {
                        singlePersonResponse = new SinglePersonResponse("Error: Invalid personID parameter", false);
                    }
                }
            }
            db.closeConnection(isCommit);
        } catch (DataAccessException e) {
            System.out.println(e);
            try {
                db.closeConnection(false);
            } catch (DataAccessException ex) {
                ex.printStackTrace();
            }
        }
        return singlePersonResponse;
    }

    public AllPersonResponse readAllPerson(String authTokenString) {
        List<Person> allFamilyMembers = new ArrayList<>();
        Database db = new Database();
        AuthTokenDao authTokenDao;
        AuthToken authToken = null;
        UserDao userDao;
        User user = null;

        PersonDao personDao;

        AllPersonResponse allPersonResponse = null;

        try {
            Connection connection = db.openConnection();
            authTokenDao = new AuthTokenDao(connection);
            authToken = authTokenDao.findAuthToken(authTokenString);

            boolean isCommit = true;
            if (authToken == null) {
                allPersonResponse = new AllPersonResponse("Error: Invalid auth token", false);
                allPersonResponse.setData(null);
                isCommit = false;

            } else {
                String username = authToken.getAssociatedUsername();
                userDao = new UserDao(connection);
                user = userDao.findUser(username);

                personDao = new PersonDao(connection);
                allFamilyMembers = personDao.findFamilyMembers(user.getUserName());
                allPersonResponse = new AllPersonResponse(allFamilyMembers, true);
            }

            db.closeConnection(isCommit);

        } catch (DataAccessException e) {
            try {
                db.closeConnection(false);
            } catch (DataAccessException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
        return allPersonResponse;
    }
}
