package response;

/**
 * Register reponses contains all of the information to be returned
 */
public class RegisterResponse {
    private String authToken;
    private String userName;
    private String personID;

    private String message;
    private boolean success = false;

    public RegisterResponse(String authToken, String userName, String personID, boolean success) {
        this.authToken = authToken;
        this.userName = userName;
        this.personID = personID;

        this.success = success;
    }

    public RegisterResponse(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getUserName() {
        return userName;
    }

    public String getPersonID() {
        return personID;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }
}
