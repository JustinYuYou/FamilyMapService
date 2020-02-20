package dao;

import databaseAccessException.DataAccessException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    private Connection connection;

    //Whenever we want to make a change to our database we will have to open a connection and use
    //Statements created by that connection to initiate transactions
    public Connection openConnection() throws DataAccessException, SQLException {
        try {
            //The Structure for this Connection is driver:language:path
            //The path assumes you start in the root of your project unless given a non-relative path
            final String dbPath = "C:\\Users\\ChenTing Yu\\IdeaProjects\\FamilyMapService\\src\\main\\database\\fms.sqlite";
            final String CONNECTION_URL = "jdbc:sqlite:" + dbPath;

            // Open a database connection to the file given in the path
            connection = DriverManager.getConnection(CONNECTION_URL);

            // Start a transaction
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            if(connection != null) {
                connection.rollback();
            }
            throw new DataAccessException("Unable to open connection to database");
        }

        return connection;
    }

    public Connection getConnection() throws DataAccessException, SQLException {
        if(connection == null) {
            return openConnection();
        } else {
            return connection;
        }
    }

    //When we are done manipulating the database it is important to close the connection. This will
    //End the transaction and allow us to either commit our changes to the database or rollback any
    //changes that were made before we encountered a potential error.

    //IMPORTANT: IF YOU FAIL TO CLOSE A CONNECTION AND TRY TO REOPEN THE DATABASE THIS WILL CAUSE THE
    //DATABASE TO LOCK. YOUR CODE MUST ALWAYS INCLUDE A CLOSURE OF THE DATABASE NO MATTER WHAT ERRORS
    //OR PROBLEMS YOU ENCOUNTER
    public void closeConnection(boolean commit) throws DataAccessException {
        try {
            if (commit) {
                //This will commit the changes to the database
                connection.commit();
            } else {
                //If we find out something went wrong, pass a false into closeConnection and this
                //will rollback any changes we made during this connection
                connection.rollback();
            }

            connection.close();
            connection = null;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Unable to close database connection");
        }
    }

    public void clearTables() throws DataAccessException {
        try (Statement stmt = connection.createStatement()){
            String sql = "DELETE FROM User";
            stmt.executeUpdate(sql);
            sql = "DELETE FROM Person";
            stmt.executeUpdate(sql);
            sql = "DELETE FROM AuthToken";
            stmt.executeUpdate(sql);
            sql = "DELETE FROM Event";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DataAccessException("SQL Error encountered while clearing tables");
        }
    }
}

