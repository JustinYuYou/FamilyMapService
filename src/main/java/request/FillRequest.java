package request;

/**
 * Request to fill number of generations for a specified user
 */
public class FillRequest {
    private String username;
    private int generation_number;

    public FillRequest(String username, int generation_number) {
        this.username = username;
        this.generation_number = generation_number;
    }

    public String getUsername() {
        return username;
    }

    public int getGeneration_number() {
        return generation_number;
    }
}
