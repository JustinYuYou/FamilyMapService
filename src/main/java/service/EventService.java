package service;

import dao.AuthTokenDao;
import dao.Database;
import dao.EventDao;
import dao.UserDao;
import databaseAccessException.DataAccessException;
import model.AuthToken;
import model.Event;
import model.Event;
import model.User;
import response.AllEventResponse;
import response.SingleEventResponse;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * URL Path: /event/[eventID]
 * Example: /event/251837d7
 * Description: Returns the single Event object with the specified ID.
 */
public class EventService {
    public SingleEventResponse readSingleEvent(String eventID) {
        Database db = new Database();
        Event event = null;
        EventDao eventDao;

        try {
            eventDao = new EventDao(db.openConnection());
            event = eventDao.findEvent(eventID);
            db.closeConnection(true);
        } catch (DataAccessException e) {

            System.out.println(e);
            try {
                db.closeConnection(false);
            } catch (DataAccessException ex) {
                ex.printStackTrace();
            }
        }

        if (event != null) {
            return new SingleEventResponse(event.getAssociatedUsername(), event.getEventID(),
                    event.getPersonID(), event.getLatitude(), event.getLongitude(), event.getCountry(),
                    event.getCity(), event.getEventType(), event.getYear(), true);
        } else {
            return new SingleEventResponse("Invalid eventID parameter", false);
        }
    }

    public AllEventResponse readAllEvent (String authTokenString) {
        List<Event> allEvents = new ArrayList<>();
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
