package service;

import dao.Database;
import dao.EventDao;
import dao.PersonDao;
import dao.UserDao;
import databaseAccessException.DataAccessException;
import model.User;
import request.FillRequest;
import response.FillResponse;
import util.GenerateFamily;

import java.sql.Connection;

/**
 * URL Path: /fill/[username]/{generations}
 * Example: /fill/susan/3
 * Description: Populates the server's database with generated data for the specified user name.
 * The required "username" parameter must be a user already registered with the server. If there is
 * any data in the database already associated with the given user name, it is deleted.
 */
public class FillService {

    public FillResponse fill(FillRequest r) {
        Database db = new Database();
        User user;
        UserDao userDao;

        PersonDao personDao;
        EventDao eventDao;

        FillResponse fillResponse = null;

        if(r.getGeneration_number() < 0) {
            return new FillResponse("Error: invalid generations parameters", false);
        }

        try {
            Connection connection = db.openConnection();
            userDao = new UserDao(connection);
            user = userDao.findUser(r.getUsername());
            boolean isCommit = true;
            if(user == null) {
                fillResponse = new FillResponse("Error: invalid username", false);
                isCommit = false;

            } else  {
                personDao = new PersonDao(connection);
                personDao.deleteUserPerson(user.getUserName());
                eventDao = new EventDao(connection);
                eventDao.deleteUserEvent(user.getUserName());

                GenerateFamily generateFamily = new GenerateFamily(user);
                generateFamily.startGenerating(r.getGeneration_number());

                int personNumber = generateFamily.getPersonCount();
                int eventNumber = generateFamily.getEventCount();

                fillResponse = new FillResponse(String.format("Successfully " +
                        "added %d persons and %d events to the database.", personNumber, eventNumber),
                        true);
            }

            db.closeConnection(isCommit);

        } catch (DataAccessException e) {
            try {
                db.closeConnection(false);
            } catch (DataAccessException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();

            fillResponse = new FillResponse("Error: when doing the load request", false);
        }

        return fillResponse;
    }
}
