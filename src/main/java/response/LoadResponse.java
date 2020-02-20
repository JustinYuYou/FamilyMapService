package response;

/**
 * The response after calling the load request call
 */
public class LoadResponse {
    private String message;
    private boolean success;

    public LoadResponse(String message, boolean success) {
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
