package service;

import request.FillRequest;
import response.FillResponse;

/**
 * URL Path: /fill/[username]/{generations}
 * Example: /fill/susan/3
 * Description: Populates the server's database with generated data for the specified user name.
 * The required "username" parameter must be a user already registered with the server. If there is
 * any data in the database already associated with the given user name, it is deleted.
 */
public class FillService {
    public FillResponse fill(FillRequest r) {
        return null;
    }
}
