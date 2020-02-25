package model;

import java.util.Objects;

/**
 * Model Authtoken class contains all the information authtoken has
 */
public class AuthToken {
    private String authToken = null;
    private String associatedUsername = null;

    public AuthToken() {
    }

    public AuthToken(String authToken, String associatedUsername) {
        this.authToken = authToken;
        this.associatedUsername = associatedUsername;
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getAssociatedUsername() {
        return associatedUsername;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public void setAssociatedUsername(String associatedUsername) {
        this.associatedUsername = associatedUsername;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthToken authToken1 = (AuthToken) o;
        return authToken.equals(authToken1.authToken) &&
                associatedUsername.equals(authToken1.associatedUsername);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authToken, associatedUsername);
    }
}
