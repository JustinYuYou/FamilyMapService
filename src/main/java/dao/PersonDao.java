package dao;

import databaseAccessException.DataAccessException;
import model.Person;
import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Database access object for Person object. It allows to retrive, insert, update, and delete person
 */
public class PersonDao {

    private Connection connection;

    public PersonDao(Connection connection) {
        this.connection = connection;
    }

    /**
     * Retrieve a person from the database.
     *
     * @param personID person to be found
     * @return the person
     */
    public Person findPerson(String personID) throws DataAccessException {
        PreparedStatement stmt = null;
        ResultSet rs = null;

        Person person;
        try {
            String sql = "select personID, associatedUsername, firstName, lastName, gender, " +
                    "fatherID, motherID, spouseID from Person where personID = ?";
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, personID);

            rs = stmt.executeQuery();

            if (rs.next()) {
                String retrievedPersonID = rs.getString(1);
                String retrievedAssociatedUsername = rs.getString(2);
                String retrievedFirstName = rs.getString(3);
                String retrievedLastName = rs.getString(4);
                String retrievedGender = rs.getString(5);
                String retrievedFatherID = rs.getString(6);
                String retrievedMotherID = rs.getString(7);
                String retrievedSpouseID = rs.getString(8);

                person = new Person(retrievedPersonID, retrievedAssociatedUsername, retrievedFirstName, retrievedLastName,
                        retrievedGender, retrievedFatherID, retrievedMotherID, retrievedSpouseID);
                return person;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while finding a person on the database");
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
     * Retrieve all of the person's for the current user
     *
     * @return persons persons to be retrieved
     * @throws SQLException if an SQL error occurs
     */
    public List<Person> findPersons(User user) throws SQLException {
        PreparedStatement stmt = null;
        ResultSet rs = null;

        Person person;
        return null;
    }

    /**
     * Add a person on the database
     *
     * @param person person to be added
     * @throws SQLException if an SQL error occurs
     */
    public void insertPerson(Person person) throws DataAccessException {
        String sql = "insert into Person (personID, associatedUsername," +
                "firstName, lastName, gender, fatherID, motherID, spouseID) " +
                "values (?,?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, person.getPersonID());
            stmt.setString(2, person.getAssociatedUsername());
            stmt.setString(3, person.getFirstName());
            stmt.setString(4, person.getLastName());
            stmt.setString(5, person.getGender());
            stmt.setString(6, person.getFatherID());
            stmt.setString(7, person.getMotherID());
            stmt.setString(8, person.getSpouseID());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while inserting Person into the database");
        }
    }

    public void insertPeople(List<Person> people) throws DataAccessException {
        for(Person person : people) {
            insertPerson(person);
        }
    }

    public void insertPersons(List<Person> persons) throws DataAccessException {
        for(Person person : persons) {
            insertPerson(person);
        }
    }

    /**
     * Delete all the persons from the database
     *
     * @throws SQLException if an SQL error occurs
     */
    public void deleteAllPersons() throws DataAccessException {
        PreparedStatement stmt = null;

        try {
            String sql = "delete from Person";
            stmt = connection.prepareStatement(sql);

            int count = stmt.executeUpdate();

            System.out.printf("Deleted %d persons\n", count);
        } catch (SQLException e) {
            System.out.println(e);
            throw new DataAccessException("Unable to delete");
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
