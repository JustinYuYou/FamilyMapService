package service;

import dao.*;
import databaseAccessException.DataAccessException;
import model.*;
import model.Event;
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
        PersonDao personDao;
        List<Person> allFamilyMembers = new ArrayList<>();

        EventDao eventDao;

        AllEventResponse allEventResponse = null;

        try {
            Connection connection = db.openConnection();

            authTokenDao = new AuthTokenDao(connection);
            authToken = authTokenDao.findAuthToken(authTokenString);
            boolean isCommit = true;
            if(authToken == null) {
                allEventResponse = new AllEventResponse("Invalid auth token", false);
                isCommit = false;
            } else {
                String username = authToken.getAssociatedUsername();
                userDao = new UserDao(connection);
                user = userDao.findUser(username);

                if(user == null) {
                    allEventResponse = new AllEventResponse("There is no user for this authToken", false);
                    isCommit = false;
                } else {

                    personDao = new PersonDao(connection);
                    allFamilyMembers = personDao.findFamilyMembers(username);

                    eventDao = new EventDao(connection);
                    for (Person member : allFamilyMembers) {
                        List<Event> eachMemberEvent = eventDao.findEvents(member.getPersonID());
                        allEvents.addAll(eachMemberEvent);
                    }

                    allEventResponse = new AllEventResponse(allEvents, true);
                }
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
        return allEventResponse;
    }
}
