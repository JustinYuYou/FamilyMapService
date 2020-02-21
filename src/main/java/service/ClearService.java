package service;

import dao.Database;
import databaseAccessException.DataAccessException;
import response.ClearResponse;

import java.sql.SQLException;

/**
 * URL Path: /clear
 * Description: Deletes ALL data from the database, including user accounts, auth tokens, and
 * generated person and event data.
 */
public class ClearService {
    public ClearResponse clear() {
        Database db = new Database();
        boolean success = true;

        try {
            db.openConnection();
            db.clearTables();
            db.closeConnection(true);
        } catch (DataAccessException e) {
            e.printStackTrace();
            try {
                db.closeConnection(false);
            } catch (DataAccessException ex) {
                ex.printStackTrace();
            }
            success = false;
        }

        if (success) {
            return new ClearResponse("Clear succeeded.", true);
        } else {
            return new ClearResponse("Internal server error", false);
        }
    }
}
