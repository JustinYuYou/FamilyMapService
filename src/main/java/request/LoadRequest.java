package request;

import model.Event;
import model.Person;
import model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * The request to load posted user, person, and event data
 */
public class LoadRequest {
    private List<User> users = new ArrayList<User>();
    private List<Person> persons = new ArrayList<Person>();
    private List<Event> events = new ArrayList<Event>();

    public LoadRequest(List<User> users, List<Person> persons, List<Event> events) {
        this.users = users;
        this.persons = persons;
        this.events = events;
    }

    public List<User> getUsers() {
        return users;
    }

    public List<Person> getPersons() {
        return persons;
    }

    public List<Event> getEvents() {
        return events;
    }
}
