package response;

/**
 * Fill response is returnted after Fill service is called
 */
public class FillResponse {
    private String message;
    private boolean success;

    public FillResponse(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }
}
