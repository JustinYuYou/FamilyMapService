package model;

import java.util.Objects;

/**
 * Model User class contains all of the information that a user has
 */
public class User {
    private String personID = null;
    private String userName = null; //Unique primary identifier
    private String password = null;
    private String email = null;
    private String firstName = null;
    private String lastName = null;
    private String gender = null;

    public User() {
    }

    public User(String userName, String personID, String password, String email, String firstName, String lastName, String gender) {
        this.userName = userName;
        this.personID = personID;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
    }

    public String getPersonID() {
        return personID;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
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

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return personID.equals(user.personID) &&
                userName.equals(user.userName) &&
                password.equals(user.password) &&
                email.equals(user.email) &&
                firstName.equals(user.firstName) &&
                lastName.equals(user.lastName) &&
                gender.equals(user.gender);
    }

    @Override
    public int hashCode() {
        return Objects.hash(personID, userName, password, email, firstName, lastName, gender);
    }
}
