package request;

/**
 * Request a single person with the specified personID
 */
public class SinglePersonRequest {
    private String personID;

    public SinglePersonRequest(String personID) {
        this.personID = personID;
    }

    public String getPersonID() {
        return personID;
    }
}
