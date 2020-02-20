package response;


import model.Event;

import java.util.ArrayList;
import java.util.List;

/**
 * Response returns all of the events
 */
public class AllEventResponse {
    private List<Event> data = new ArrayList<>();

    private String message;
    private boolean success;

    public AllEventResponse(List<Event> data, boolean successs) {
        this.data = data;
        this.success = successs;
    }

    public AllEventResponse(String message, boolean successs) {
        this.message = message;
        this.success = successs;
    }

    public List<Event> getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }
}
