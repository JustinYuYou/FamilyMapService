package service;

import dao.Database;
import dao.EventDao;
import databaseAccessException.DataAccessException;
import model.Event;
import response.SingleEventResponse;

import java.sql.SQLException;

/**
 * URL Path: /event/[eventID]
 * Example: /event/251837d7
 * Description: Returns the single Event object with the specified ID.
 */
public class SingleEventService {
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
}
