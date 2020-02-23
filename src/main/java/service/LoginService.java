package service;

import dao.Database;
import dao.UserDao;
import databaseAccessException.DataAccessException;
import handler.LoginHandler;
import model.AuthToken;
import model.User;
import request.LoginRequest;
import response.LoginResponse;

/**
 * URL Path: /user/login
 * Description: Logs in the user and returns an auth token.
 */
public class LoginService {
    public LoginResponse login(LoginRequest r, String authToken){
        Database db = new Database();
        UserDao userDao;
        User user = null;

        try {
            userDao = new UserDao(db.openConnection());
            user = userDao.findUser(r.getUserName());

            if(user.getPassword() != r.getPassword()) {
                return new LoginResponse("Invalid password", false);
            }
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return new LoginResponse(authToken , user.getUserName(), user.getPersonID(), true);
    }
    private AuthToken generateAuthToken() {
        return null;
    }
}
