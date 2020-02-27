package util;

import com.google.gson.Gson;
import dao.Database;
import dao.EventDao;
import dao.PersonDao;
import databaseAccessException.DataAccessException;
import model.Event;
import model.Person;
import org.graalvm.compiler.replacements.Log;


import java.io.*;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GenerateFamily {

    private String username;
    private Random random = new Random();
    private Gson gson = new Gson();

    private int personBirthYear;

    private List<Person> familyMembers = new ArrayList<>();
    private List<Event> familyMembersEvents = new ArrayList<>();

    public GenerateFamily(String username) {
        this.username = username;
    }

    public void generateFamily(Person root, int generations) {

    }

    private Person createFather(Person child) {
        Person father = new Person();
        String fatherID = GenerateRandom.generateRandomString();
        String lastName;

        father.setAssociatedUsername(child.getAssociatedUsername());
        father.setPersonID(fatherID);
        father.setFirstName(generateMaleName());
        father.setGender("m");

        if(child.getGender().equals('m')) {
            lastName = child.getLastName();
        } else {
            lastName = generateLastName();
        }

        //Create birth event
        int birthYear = personBirthYear - 13 - random.nextInt(27);
        Event birth = createEvent(fatherID, this.username, birthYear);

        //Create marriage event
        int marriageYear = birthYear + 20 + random.nextInt(10);
        Event marriage = createEvent(fatherID, "Marriage", marriageYear);

        //Create death event
        int deathYear = personBirthYear + random.nextInt(50);
        Event death = createEvent(fatherID, "Death", deathYear);


        return father;
    }


    private Person createMother(Person child) {

        Person mother = new Person();
        String motherID = GenerateRandom.generateRandomString();
        String lastName;

        mother.setAssociatedUsername(child.getAssociatedUsername());
        mother.setPersonID(motherID);
        mother.setFirstName(generateFemaleName());
        mother.setGender("f");


        return mother;

    }

    private String generateMaleName () {
        File file = new File("C:\\Users\\ChenTing Yu\\IdeaProjects\\FamilyMapService\\json\\mnames.json");
//        try(FileReader fileReader = new FileReader(file);
//             BufferedReader bufferedReader = new BufferedReader(fileReader)) {
//
//            JsonToken tokener = new JsonToken(bufferedReader);
//            JsonObject rootObj = new JsonObject();
//
//            JsonArray cdArr = (JsonArray) rootObj.get("data");
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }

        return null;
    }
    private String generateFemaleName () {
        return null;
    }

    private String generateLastName() {
        return null;
    }

    private Event createEvent(String personID, String eventType, int year) {

        String eventID = GenerateRandom.generateRandomString();
        String locationJson = generateLocation();

        Event event = gson.fromJson(locationJson, Event.class);
        event.setAssociatedUsername(this.username);
        event.setEventID(eventID);
        event.setPersonID(personID);
        event.setEventType(eventType);
        event.setYear(year);

        return event;
    }

    private String generateLocation() {
        return null;
    }

    private void addToDatabase() {
        Database db = new Database();

        try {
            Connection connection = db.openConnection();
            PersonDao personDao = new PersonDao(connection);
            EventDao eventDao = new EventDao(connection);

            personDao.insertPersons(familyMembers);
            eventDao.insertEvents(familyMembersEvents);

            db.closeConnection(true);
        } catch (DataAccessException e) {

            try {
                db.closeConnection(false);
            } catch (DataAccessException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
    }
}
