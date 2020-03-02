package service;

import dao.*;
import databaseAccessException.DataAccessException;
import model.*;
import model.Event;
import response.AllEventResponse;
import response.SingleEventResponse;
import response.SinglePersonResponse;

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
    public SingleEventResponse readSingleEvent(String eventID, String authTokenString) {
        Database db = new Database();
        Event event = null;
        EventDao eventDao;
        AuthTokenDao authTokenDao;
        AuthToken authToken;
        SingleEventResponse singleEventResponse = null;
        try {
            Connection connection = db.openConnection();
            authTokenDao = new AuthTokenDao(connection);
            authToken = authTokenDao.findAuthToken(authTokenString);
            boolean isCommit = true;
            if (authToken == null) {
                singleEventResponse = new SingleEventResponse("Error: invalid auth token", false);
                isCommit = false;
            } else {
                eventDao = new EventDao(connection);
                event = eventDao.findEvent(eventID);

                if(event == null) {
                    singleEventResponse = new SingleEventResponse("Error: the event doesn't belong to this person", false);
                    isCommit = false;
                }else if (!authToken.getAssociatedUsername().equals(event.getAssociatedUsername())) {
                    singleEventResponse = new SingleEventResponse("Error: the event doesn't belong to this person", false);
                    isCommit = false;
                } else{
                    if (event != null) {
                        singleEventResponse = new SingleEventResponse(event.getAssociatedUsername(), event.getEventID(),
                                event.getPersonID(), event.getLatitude(), event.getLongitude(), event.getCountry(),
                                event.getCity(), event.getEventType(), event.getYear(), true);
                    } else {
                        singleEventResponse = new SingleEventResponse("Error: Invalid eventID parameter", false);
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


        return singleEventResponse;

    }

    public AllEventResponse readAllEvent(String authTokenString) {
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
            if (authToken == null) {
                allEventResponse = new AllEventResponse("Error: Invalid auth token", false);
                allEventResponse.setData(null);
                isCommit = false;
            } else {
                String username = authToken.getAssociatedUsername();
                userDao = new UserDao(connection);
                user = userDao.findUser(username);

                if (user == null) {
                    allEventResponse = new AllEventResponse("Error: There is no user for this authToken", false);
                    allEventResponse.setData(null);
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
