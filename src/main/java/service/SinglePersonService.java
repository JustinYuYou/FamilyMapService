package service;

import dao.Database;
import dao.PersonDao;
import databaseAccessException.DataAccessException;
import model.Person;
import request.SinglePersonRequest;
import response.SinglePersonResponse;

import java.sql.SQLException;

/**
 * URL Path: /person/[personID]
 * Example: /person/7255e93e
 * Description: Returns the single Person object with the specified ID.
 */
public class SinglePersonService {
    public SinglePersonResponse readSinglePerson(SinglePersonRequest r) {
        Database db = new Database();
        PersonDao personDao = null;
        Person person = null;
        try {
            db.openConnection();
            person = personDao.findPerson(r.getPersonID());
            db.closeConnection(true);
        } catch (DataAccessException e) {
            System.out.println(e);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(person != null){
            return new SinglePersonResponse(person.getPersonID(), person.getAssociatedUsername(),
                    person.getFirstName(), person.getLastName(), person.getGender(), person.getFatherID(),
                    person.getMotherID(), person.getSpouseID(), true);
        } else {
            return new SinglePersonResponse("Unable to retrieve the person with id " + r.getPersonID(), false);
        }
    }
}
