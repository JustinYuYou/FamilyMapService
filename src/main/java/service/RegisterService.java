package service;

import dao.AuthTokenDao;
import dao.Database;
import dao.PersonDao;
import dao.UserDao;
import databaseAccessException.DataAccessException;
import model.AuthToken;
import model.Person;
import model.User;
import request.RegisterRequest;
import response.RegisterResponse;
import util.GenerateFamily;
import util.GenerateRandom;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

/**
 * Creates a new user account, generates 4 generations of ancestor data for the new
 * user, logs the user in, and returns an auth token.
 *
 * URL Path: /user/register
 */
public class RegisterService {

    public RegisterResponse register(RegisterRequest r) {
        Database db = new Database();
        UserDao userDao;
        User user;

        AuthTokenDao authTokenDao;
        AuthToken authToken;

        GenerateFamily familyGenerator;
        RegisterResponse registerResponse;

        try {
            Connection connection = db.openConnection();
            userDao = new UserDao(connection);

            String personID = GenerateRandom.generateRandomString();
            user = new User(r.getUserName(), personID, r.getPassword(), r.getEmail(),
                    r.getFirstName(), r.getLastName(), r.getGender());
            userDao.insertUser(user);

            authTokenDao = new AuthTokenDao(connection);
            String authTokenString = GenerateRandom.generateRandomString();
            authToken = new AuthToken(authTokenString, user.getUserName());
            authTokenDao.insertAuthToken(authToken);
            db.closeConnection(true);

            familyGenerator = new GenerateFamily(user);
            familyGenerator.startGenerating(4);

            registerResponse = new RegisterResponse(authTokenString, user.getUserName()
                    , user.getPersonID(), true);

        } catch (DataAccessException e) {
            try {
                db.closeConnection(false);
            } catch (DataAccessException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            registerResponse = new RegisterResponse("Error: Request property missing or has invalid value",
                    false);
        }

        return registerResponse;
    }


}
