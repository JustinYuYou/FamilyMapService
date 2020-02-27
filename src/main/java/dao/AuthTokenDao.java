package dao;

import databaseAccessException.DataAccessException;
import model.AuthToken;

import java.io.File;
import java.sql.*;

/**
 * Database access object for AuthToken object. It allows to retrieve, insert, update, and delete token
 */
public class AuthTokenDao {
    private Connection connection;

    public AuthTokenDao(Connection connection) {
        this.connection = connection;
    }

    /**
     * Retrieve one token from the database.
     *
     * @param authTokenToBeGet the token to be retrieved
     * @return the authToken
     * @throws SQLException if an SQL error occurs
     */
    public AuthToken findAuthToken(String authTokenToBeGet) throws DataAccessException {

        PreparedStatement stmt = null;
        ResultSet rs = null;

        AuthToken authToken = null;

        try {
            String sql = "select authToken, associatedUsername from AuthToken " +
                    "where authToken = ?";
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, authTokenToBeGet);

            rs = stmt.executeQuery();
            if (rs.next()) {
                String retrievedAuth = rs.getString(1);
                String retrievedName = rs.getString(2);

                authToken = new AuthToken(retrievedAuth, retrievedName);
                return authToken;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while finding a authToken on the database");
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
     * Insert a token to the database
     *
     * @param authToken the token to be inserted
     * @throws SQLException if an SQL error occurs
     */
    public void insertAuthToken(AuthToken authToken) throws DataAccessException {
        String sql = "insert into AuthToken (authToken, associatedUsername) values (?,?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, authToken.getAuthToken());
            stmt.setString(2, authToken.getAssociatedUsername());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while inserting an authToken into the database");
        }
    }

//    /**
//     * Update an authentication token for a user on the database
//     *
//     * @param username the user
//     * @throws SQLException if an SQL error occurs
//     */
//    public void updateToken(String newAuthTokenString, String username) throws DataAccessException {
//        String sql = "update AuthToken set authToken = ? where associatedUsername = ?";
//        PreparedStatement stmt = null;
//
//        try {
//            stmt = connection.prepareStatement(sql);
//            stmt.setString(1, newAuthTokenString);
//            stmt.setString(2, username);
//
//            if(stmt.executeUpdate() == 1) {
//                System.out.println("Successfully update the token");
//            } else {
//                System.out.println("Failed to update the token");
//            }
//        } catch (SQLException e) {
//            throw new DataAccessException("Error encountered while updating an authToken into the database");
//        } finally {
//            if (stmt != null) {
//                try {
//                    stmt.close();
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }


    /**
     * delete all the tokens on the database
     *
     * @throws SQLException if an SQL error occurs
     */
    public void deleteAllAuthTokens() {
        PreparedStatement stmt = null;

        try {
            String sql = "delete from AuthToken";
            stmt = connection.prepareStatement(sql);

            int count = stmt.executeUpdate();

            System.out.printf("Deleted %d authTokens\n", count);
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
