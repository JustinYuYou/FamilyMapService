package service;

import dao.AuthTokenDao;
import dao.Database;
import dao.UserDao;
import databaseAccessException.DataAccessException;
import handler.LoginHandler;
import model.AuthToken;
import model.User;
import request.LoginRequest;
import response.LoginResponse;
import util.GenerateRandom;

import java.sql.Connection;

/**
 * URL Path: /user/login
 * Description: Logs in the user and returns an auth token.
 */
public class LoginService {
    public LoginResponse login(LoginRequest r){
        Database db = new Database();
        UserDao userDao;
        AuthTokenDao authTokenDao;
        User user = null;
        String authTokenString;
        AuthToken authToken;
        try {
            Connection connection = db.openConnection();
            userDao = new UserDao(connection);
            user = userDao.findUser(r.getUserName());

            if(user == null) {
                db.closeConnection(false);
                return new LoginResponse("Error: User does not exist", false);
            }
            if(!user.getPassword().equals(r.getPassword())) {
                db.closeConnection(false);
                return new LoginResponse("Error: Invalid password", false);
            }
            authTokenString = GenerateRandom.generateRandomString();
            authTokenDao = new AuthTokenDao(connection);
            authToken = new AuthToken(authTokenString, user.getUserName());
            authTokenDao.insertAuthToken(authToken);
            db.closeConnection(true);
        } catch (DataAccessException e) {
            try {
                db.closeConnection(false);
            } catch (DataAccessException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return new LoginResponse("Error when trying to log in", false);
        }

        return new LoginResponse(authTokenString , user.getUserName(), user.getPersonID(), true);
    }
}
