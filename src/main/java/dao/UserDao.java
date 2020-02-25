package dao;


import databaseAccessException.DataAccessException;
import model.User;

import java.io.File;
import java.sql.*;
import java.util.List;

/**
 * Database access object for User object. It allows to retrieve, insert, update, and delete user
 */
public class UserDao {
    private Connection connection;

    public UserDao(Connection connection) {
        this.connection = connection;
    }

    /**
     * Retrieve a user from the database.
     *
     * @param username the username
     * @return the user
     * @throws SQLException if an SQL error occurs
     */
    public User findUser(String username) throws DataAccessException {

        PreparedStatement stmt = null;
        ResultSet rs = null;

        User user = null;
        try {
            String sql = "select userName, personID, password, email, " +
                    "firstName, lastName, gender from User where userName = " + username;
            stmt = connection.prepareStatement(sql);

            rs = stmt.executeQuery();

            if (rs.next()) {
                String userName = rs.getString(1);
                String personID = rs.getString(2);
                String password = rs.getString(3);
                String email = rs.getString(4);
                String firstName = rs.getString(5);
                String lastName = rs.getString(6);
                String gender = rs.getString(7);

                user = new User(userName, personID, password, email, firstName, lastName, gender);
                return user;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while finding a user on the database");
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
     * Add a user on the database
     *
     * @param user the user to be inserted
     * @throws SQLException if an SQL error occurs
     */
    public void insertUser(User user) throws DataAccessException {
        String sql = "insert into User (userName, personID," +
                "password, email, firstName, lastName, gender) " +
                "values (?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            //Using the statements built-in set(type) functions we can pick the question mark we want
            //to fill in and give it a proper value. The first argument corresponds to the first
            //question mark found in our sql String
            stmt.setString(1, user.getUserName());
            stmt.setString(2, user.getPersonID());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getEmail());
            stmt.setString(5, user.getFirstName());
            stmt.setString(6, user.getLastName());
            stmt.setString(7, user.getGender());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while inserting into the database");
        }
    }
    public void insertUsers(List<User> users) throws DataAccessException {
        for(User user : users) {
            insertUser(user);
        }
    }

    /**
     * Delete all the users from the database
     *
     * @throws SQLException if an SQL error occurs
     */
    public void deleteAllUsers() {
        PreparedStatement stmt = null;

        try {
            String sql = "delete from User";
            stmt = connection.prepareStatement(sql);

            int count = stmt.executeUpdate();

            System.out.printf("Deleted %d users\n", count);
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

}
