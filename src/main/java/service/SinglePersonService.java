package service;

import dao.Database;
import dao.PersonDao;
import databaseAccessException.DataAccessException;
import model.Person;
import response.SinglePersonResponse;

import java.sql.SQLException;

/**
 * URL Path: /person/[personID]
 * Example: /person/7255e93e
 * Description: Returns the single Person object with the specified ID.
 */
public class SinglePersonService {
    public SinglePersonResponse readSinglePerson(String personID) {
        Database db = new Database();
        PersonDao personDao;
        Person person = null;

        try {
            personDao = new PersonDao(db.openConnection());
            person = personDao.findPerson(personID);
            db.closeConnection(true);
        } catch (DataAccessException | SQLException e) {
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
}
