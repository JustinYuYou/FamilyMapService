package service;

import model.AuthToken;
import request.LoginRequest;
import response.LoginResponse;

/**
 * URL Path: /user/login
 * Description: Logs in the user and returns an auth token.
 */
public class LoginService {
    private AuthToken authToken;
    public LoginResponse login(LoginRequest r){
        return null;
    }
    private AuthToken generateAuthToken() {
        return null;
    }
}
