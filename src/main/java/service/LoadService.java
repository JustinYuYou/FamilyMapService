package service;

import dao.Database;
import dao.EventDao;
import dao.PersonDao;
import dao.UserDao;
import databaseAccessException.DataAccessException;
import model.Event;
import model.Person;
import model.User;
import request.LoadRequest;
import response.LoadResponse;

import java.sql.Connection;
import java.util.List;

/**
 * URL Path: /load
 * Description: Clears all data from the database (just like the /clear API), and then loads the
 * posted user, person, and event data into the database.
 */
public class LoadService {
    public LoadResponse load(LoadRequest r){
        Database db = new Database();
        UserDao userDao;
        PersonDao personDao;
        EventDao eventDao;

        List<User> users = r.getUsers();
        List<Person> persons = r.getPersons();
        List<Event> events = r.getEvents();

        try {
            Connection connection = db.openConnection();

            db.clearTables();

            userDao = new UserDao(connection);
            userDao.insertUsers(users);
            System.out.printf("Users have been successfully put into the database\n");

            personDao = new PersonDao(connection);
            personDao.insertPersons(persons);
            System.out.printf("Persons have been successfully put into the database\n");

            eventDao = new EventDao(connection);
            eventDao.insertEvents(events);
            System.out.printf("Events have been successfully put into the database\n");

            db.closeConnection(true);
        } catch (DataAccessException e) {
            try {
                db.closeConnection(false);
            } catch (DataAccessException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();

            return new LoadResponse("Error when doing the load request", false);
        }
        return new LoadResponse(String.format("Successfully added %d users, %d persons, and %d events to the database.",
                users.size(), persons.size(), events.size()), true);
    }
}
