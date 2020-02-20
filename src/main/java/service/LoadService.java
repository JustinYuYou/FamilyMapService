package service;

import request.LoadRequest;
import response.LoadResponse;

/**
 * URL Path: /load
 * Description: Clears all data from the database (just like the /clear API), and then loads the
 * posted user, person, and event data into the database.
 */
public class LoadService {
    public LoadResponse load(LoadRequest r){
        return null;
    }
}
