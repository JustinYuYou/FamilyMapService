package response;

/**
 * Response after the clear service is called
 */
public class ClearResponse {
    private String message;
    private boolean success;

    public ClearResponse(String message, boolean success) {
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
