package model;

import java.util.Objects;

/**
 * Model Person class contains all of the information a person has
 */
public class Person {


    private String personID = null;
    private String associatedUsername = null;
    private String firstName = null;
    private String lastName = null;
    private String gender = null;
    private String fatherID = null;
    private String motherID = null;
    private String spouseID = null;

    public Person() {
    }

    public Person(String personID, String associatedUsername,
                  String firstName, String lastName, String gender) {
        this.personID = personID;
        this.associatedUsername = associatedUsername;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
    }

    public Person(String personID, String associatedUsername, String firstName, String lastName,
                  String gender, String fatherID, String motherID, String spouseID) {
        this.personID = personID;
        this.associatedUsername = associatedUsername;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.fatherID = fatherID;
        this.motherID = motherID;
        this.spouseID = spouseID;
    }

    public String getPersonID() {
        return personID;
    }

    public String getAssociatedUsername() {
        return associatedUsername;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getGender() {
        return gender;
    }

    public String getFatherID() {
        return fatherID;
    }

    public String getMotherID() {
        return motherID;
    }

    public String getSpouseID() {
        return spouseID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public void setAssociatedUsername(String associatedUsername) {
        this.associatedUsername = associatedUsername;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setFatherID(String fatherID) {
        this.fatherID = fatherID;
    }

    public void setMotherID(String motherID) {
        this.motherID = motherID;
    }

    public void setSpouseID(String spouseID) {
        this.spouseID = spouseID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return personID.equals(person.personID) &&
                associatedUsername.equals(person.associatedUsername) &&
                firstName.equals(person.firstName) &&
                lastName.equals(person.lastName) &&
                gender.equals(person.gender) &&
                Objects.equals(fatherID, person.fatherID) &&
                Objects.equals(motherID, person.motherID) &&
                Objects.equals(spouseID, person.spouseID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(personID, associatedUsername, firstName, lastName, gender, fatherID, motherID, spouseID);
    }
}
