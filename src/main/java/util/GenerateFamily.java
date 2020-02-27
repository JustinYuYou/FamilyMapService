package util;

import com.google.gson.Gson;
import dao.Database;
import dao.EventDao;
import dao.PersonDao;
import databaseAccessException.DataAccessException;
import model.Event;
import model.Person;
import org.graalvm.compiler.replacements.Log;
import org.w3c.dom.NameList;


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

    //Store it so we can pass it to mother to have the same marriage event as the father
    private Event marriage = new Event();

    private List<Person> familyMembers = new ArrayList<>();
    private List<Event> familyMembersEvents = new ArrayList<>();

    public GenerateFamily() {
    }

    public GenerateFamily(String username) {
        this.username = username;
    }

    public void startGenerating() {

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
        Event birth = createEvent(fatherID, "Birth", birthYear);

        //Create marriage event
        int marriageYear = birthYear + 20 + random.nextInt(10);
        marriage = createEvent(fatherID, "Marriage", marriageYear);

        //Create death event
        int deathYear = personBirthYear + random.nextInt(50);
        Event death = createEvent(fatherID, "Death", deathYear);




        return father;
    }


    private Person createMother(Person father) {

        Person mother = new Person();
        String motherID = GenerateRandom.generateRandomString();
        String lastName = father.getLastName();

        mother.setAssociatedUsername(this.username);
        mother.setPersonID(motherID);
        mother.setFirstName(generateFemaleName());
        mother.setLastName(lastName);
        mother.setGender("f");

        //Create birth event
        int birthYear = personBirthYear - 13 - random.nextInt(27);
        Event birth = createEvent(motherID, "Birth", birthYear);

        //Create marriage event
        String eventID = GenerateRandom.generateRandomString();
        Event marriageOfMother = new Event(eventID, this.username, motherID, marriage.getLatitude(),
                            marriage.getLongitude(), marriage.getCountry(), marriage.getCity(),
                            marriage.getEventType(), marriage.getYear());

        //Create death event
        int deathYear = personBirthYear + random.nextInt(50);
        Event death = createEvent(motherID, "Death", deathYear);


        return mother;

    }

    private String generateMaleName () {
        File file = new File("C:\\Users\\ChenTing Yu\\IdeaProjects\\FamilyMapService\\json\\mnames.json");
        String maleName = "";
        try(FileReader fileReader = new FileReader(file);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {

           ListOfNames listOfNames = gson.fromJson(bufferedReader, ListOfNames.class);
           int length = listOfNames.getData().size();
            maleName = listOfNames.getData().get(random.nextInt(length));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return maleName;
    }
    private String generateFemaleName () {
        File file = new File("C:\\Users\\ChenTing Yu\\IdeaProjects\\FamilyMapService\\json\\fnames.json");
        String femaleName = "";

        try(FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            ListOfNames listOfNames = gson.fromJson(bufferedReader, ListOfNames.class);
            int length = listOfNames.getData().size();
            femaleName = listOfNames.getData().get(random.nextInt(length));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return femaleName;
    }

    private String generateLastName() {
        File file = new File("C:\\Users\\ChenTing Yu\\IdeaProjects\\FamilyMapService\\json\\snames.json");
        String lastName = "";

        try(FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            ListOfNames listOfNames = gson.fromJson(bufferedReader, ListOfNames.class);
            int length = listOfNames.getData().size();
            lastName = listOfNames.getData().get(random.nextInt(length));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lastName;
    }

    private Event createEvent(String personID, String eventType, int year) {

        String eventID = GenerateRandom.generateRandomString();

        Event event = generateLocation();
        event.setAssociatedUsername(this.username);
        event.setEventID(eventID);
        event.setPersonID(personID);
        event.setEventType(eventType);
        event.setYear(year);

        return event;
    }

    public Event generateLocation() {
        File file = new File("C:\\Users\\ChenTing Yu\\IdeaProjects\\FamilyMapService\\json\\locations.json");
        Event location = new Event();

        try(FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            ListOfEvents listOfEvents = gson.fromJson(bufferedReader, ListOfEvents.class);
            int length = listOfEvents.getData().size();
            location = listOfEvents.getData().get(random.nextInt(length));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return location;
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
