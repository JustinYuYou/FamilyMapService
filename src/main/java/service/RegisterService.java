package service;

import dao.Database;
import dao.UserDao;
import databaseAccessException.DataAccessException;
import model.Person;
import model.User;
import request.RegisterRequest;
import response.RegisterResponse;
import util.GenerateRandom;

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
        List<Person> ancestors = new ArrayList<Person>();

        try {
            userDao = new UserDao(db.openConnection());
            String personID = GenerateRandom.generateRandomString();
            user = new User(r.getUserName(), personID, r.getPassword(), r.getEmail(),
                    r.getFirstName(), r.getLastName(), r.getGender());

            userDao.insertUser(user);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }

        return null;
    }


}
