package dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import databaseAccessException.DataAccessException;
import model.Event;
import model.Person;
import model.User;

/**
 * Database access object for Event object. It allows to retrive, insert, update, and delete user
 */
public class EventDao {
    private Connection connection;

    public EventDao(Connection connection) {
        this.connection = connection;
    }

    /**
     * Retrieve an event from the database
     *
     * @param eventID event to be found
     * @return the events
     */
    public Event findEvent(String eventID) throws DataAccessException {
        PreparedStatement stmt = null;
        ResultSet rs = null;

        Event event;
        try {
            String sql = "select eventID, associatedUsername, personID, latitude, " +
                    "longitude, country, city, eventType, year from Event where eventID = ?";
            stmt = connection.prepareStatement(sql);
            stmt.setString(1,  eventID);

            rs = stmt.executeQuery();

            if (rs.next()) {
                String retrievedEventID = rs.getString(1);
                String retrievedAssociatedUsername = rs.getString(2);
                String retrievedPersonID = rs.getString(3);
                float retrievedLatitude = rs.getFloat(4);
                float retrievedLongitude = rs.getFloat(5);
                String retrievedCountry = rs.getString(6);
                String retrievedCity = rs.getString(7);
                String retrievedEventType = rs.getString(8);
                int retrievedYear = rs.getInt(9);

                event = new Event(retrievedEventID, retrievedAssociatedUsername, retrievedPersonID,
                        retrievedLatitude, retrievedLongitude, retrievedCountry, retrievedCity,
                        retrievedEventType, retrievedYear);
                return event;

            }
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while finding an event on the database");
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * Retrieve a set of events from the database
     *
     * @param personID the person's events to be retrieved
     * @return the events
     * @throws SQLException if an SQL error occurs
     */
    public List<Event> findEvents(String personID) throws DataAccessException {
        PreparedStatement stmt = null;
        ResultSet rs = null;

        List<Event> events = new ArrayList<>();

        try {
            String sql = "select eventID, associatedUsername, personID, " +
                    "latitude, longitude, country, city, eventType, year " +
                    "from Event where personID = ? ";
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, personID);

            rs = stmt.executeQuery();
            while(rs.next()) {
                String eventID = rs.getString(1);
                String associatedUsername = rs.getString(2);
                String retrievedPersonID = rs.getString(3);
                float latitude = rs.getFloat(4);
                float longitude = rs.getFloat(5);
                String country = rs.getString(6);
                String city = rs.getString(7);
                String eventType = rs.getString(8);
                int year = rs.getInt(9);

                events.add(new Event(eventID, associatedUsername, retrievedPersonID,
                        latitude, longitude, country, city, eventType, year));
            }

        }  catch (SQLException e) {
            throw new DataAccessException("Error encountered while finding events on the database");
        }  finally {
            if(rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        if(events.isEmpty()) {
            return null;
        } else {
            return events;
        }
    }

    /**
     * Add an event on the database
     *
     * @param event event to be inserted
     * @throws SQLException if an SQL error occurs
     */
    public void insertEvent(Event event) throws DataAccessException {
        String sql = "insert into Event (eventID, associatedUsername, personID, " +
                "latitude, longitude, country, city, eventType, year) " +
                "values (?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, event.getEventID());
            stmt.setString(2, event.getAssociatedUsername());
            stmt.setString(3, event.getPersonID());
            stmt.setFloat(4, event.getLatitude());
            stmt.setFloat(5, event.getLongitude());
            stmt.setString(6, event.getCountry());
            stmt.setString(7, event.getCity());
            stmt.setString(8, event.getEventType());
            stmt.setInt(9, event.getYear());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while inserting Event into the database");
        }
    }

    public void insertEvents(List<Event> events) throws DataAccessException {
        for(Event event : events) {
            insertEvent(event);
        }
    }

    /**
     * Delete all the events from the database
     *
     * @throws SQLException if an SQL error occurs
     */
    public void deleteAllEvents() throws DataAccessException {
        PreparedStatement stmt = null;

        try {
            String sql = "delete from Event";
            stmt = connection.prepareStatement(sql);

            int count = stmt.executeUpdate();

            System.out.printf("Deleted %d events\n", count);
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void deleteUserEvent(String username) throws DataAccessException {
        PreparedStatement stmt = null;

        try {
            String sql = "delete from Event where associatedUsername = ?";
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, username);
            int count = stmt.executeUpdate();

            System.out.printf("Related %d events are deleted", count);

        } catch (SQLException e) {
            System.out.println(e);
            throw new DataAccessException("Unable to delete related event");
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        }
    }


}
