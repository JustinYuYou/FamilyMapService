package response;


import model.Person;

import java.util.ArrayList;
import java.util.List;

/**
 * Response returns all of the members
 */
public class AllPersonResponse {
    private List<Person> data = new ArrayList<>();

    private String message;
    private boolean success;

    public AllPersonResponse() {
    }

    public AllPersonResponse(List<Person> data, boolean success) {
        this.data = data;
        this.success = success;
    }

    public AllPersonResponse(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    public List<Person> getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setData(List<Person> data) {
        this.data = data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
