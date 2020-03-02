package util;

import com.google.gson.Gson;
import dao.Database;
import dao.EventDao;
import dao.PersonDao;
import databaseAccessException.DataAccessException;
import model.Event;
import model.Person;
import model.User;
import org.graalvm.compiler.replacements.Log;
import org.w3c.dom.NameList;


import java.io.*;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GenerateFamily {

    //Utility object
    private Random random = new Random();
    private Gson gson = new Gson();

    private User user;
    private Person root;
    private int personBirthYear = 1960 + random.nextInt(40);
    private int fatherBirthYear;
    private int motherBirthYear;

    //Store it so we can pass it to mother to have the same marriage event as the father
    private Event marriage = new Event();

    private List<Person> familyMembers = new ArrayList<>();
    private List<Event> familyMembersEvents = new ArrayList<>();

    private int personCount = 0;
    private int eventCount = 0;

    public int getPersonCount() {
        return personCount;
    }

    public int getEventCount() {
        return eventCount;
    }

    public GenerateFamily() {

    }

    public GenerateFamily(User user) {
        this.user = user;
    }

    public void startGenerating(int generations) {

        createRoot();
        generateFamily(root, generations, personBirthYear);

        personCount = familyMembers.size();
        eventCount = familyMembersEvents.size();

        addToDatabase();

    }
    public void generateFamily(Person curPerson, int generations, int curBirthYear) {
        if(generations == 0) {
            return;
        }
        int fbirthyear = curBirthYear - 13 - random.nextInt(13);
        int mbirthyear = curBirthYear - 13 - random.nextInt(13);

        Person father = createFather(curPerson, fbirthyear);
        curPerson.setFatherID(father.getPersonID());
        Person mother = createMother(curPerson, mbirthyear);
        curPerson.setMotherID(mother.getPersonID());



        father.setSpouseID(mother.getPersonID());
        mother.setSpouseID(father.getPersonID());

        familyMembers.add(father);
        familyMembers.add(mother);

        generations--;

//        Database db = new Database();

//        try {
//            Connection connection = db.openConnection();
//            EventDao eventDao = new EventDao(connection);
//            fbirthyear = eventDao.findEvent(father.getPersonID()).getYear();
//            mbirthyear = eventDao.findEvent(mother.getPersonID()).getYear();
//            db.closeConnection(true);
//        } catch (DataAccessException e) {
//            try {
//                db.closeConnection(false);
//            } catch (DataAccessException ex) {
//                ex.printStackTrace();
//            }
//            e.printStackTrace();
//        }
        generateFamily(father, generations, fbirthyear);
        generateFamily(mother, generations, mbirthyear);

    }

    public void createRoot() {
        root = new Person(user.getPersonID(), user.getUserName(),
                user.getFirstName(), user.getLastName(), user.getGender());
        Event birth = createEvent(root.getPersonID(), "Birth", personBirthYear);

        familyMembers.add(root);
        familyMembersEvents.add(birth);
    }

    private Person createFather(Person child, int childBirthYear) {
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
        father.setLastName(lastName);

        //Create birth event
//        int birthYear = childBirthYear - 13 - random.nextInt(20);
//        fatherBirthYear = birthYear;
        Event birth = createEvent(fatherID, "Birth", childBirthYear);
        familyMembersEvents.add(birth);

        //Create marriage event
        int marriageYear = childBirthYear + 20 + random.nextInt(10);
        marriage = createEvent(fatherID, "Marriage", marriageYear);
        familyMembersEvents.add(marriage);

        //Create death event
        int deathYear = childBirthYear + random.nextInt(50);
        Event death = createEvent(fatherID, "Death", deathYear);
        familyMembersEvents.add(death);


        return father;
    }


    private Person createMother(Person father, int childBirthYear) {

        Person mother = new Person();
        String motherID = GenerateRandom.generateRandomString();
        String lastName = father.getLastName();

        mother.setAssociatedUsername(this.user.getUserName());
        mother.setPersonID(motherID);
        mother.setFirstName(generateFemaleName());
        mother.setLastName(lastName);
        mother.setGender("f");

        //Create birth event
//        int birthYear = childBirthYear - 13 - random.nextInt(10);
//        motherBirthYear = birthYear;
        Event birth = createEvent(motherID, "Birth", childBirthYear);
        familyMembersEvents.add(birth);

        //Create marriage event
        String eventID = GenerateRandom.generateRandomString();
        Event marriageOfMother = new Event(eventID, this.user.getUserName(), motherID, marriage.getLatitude(),
                            marriage.getLongitude(), marriage.getCountry(), marriage.getCity(),
                            marriage.getEventType(), marriage.getYear());
        familyMembersEvents.add(marriageOfMother);

        //Create death event
        int deathYear = childBirthYear + random.nextInt(50);
        Event death = createEvent(motherID, "Death", deathYear);
        familyMembersEvents.add(death);


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
        event.setAssociatedUsername(this.user.getUserName());
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


            int i = 0;
            for(Person member : familyMembers) {
                personDao.insertPerson(member);
                System.out.println(String.format("We have %d people successfully inserted", i + 1));
                i++;
            }

            int j = 0;
            for(Event event : familyMembersEvents) {
                eventDao.insertEvent(event);
                System.out.println(String.format("We have %d events successfully inserted", j + 1));
                j++;
            }

            db.closeConnection(true);
        } catch (DataAccessException e) {

            try {
                db.closeConnection(false);
            } catch (DataAccessException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
