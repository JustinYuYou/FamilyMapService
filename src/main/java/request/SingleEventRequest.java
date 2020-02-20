package request;

/**
 * Request an event with a specified eventID
 */
public class SingleEventRequest {
    private String eventID;

    public SingleEventRequest(String eventID) {
        this.eventID = eventID;
    }

    public String getEventID() {
        return eventID;
    }
}
