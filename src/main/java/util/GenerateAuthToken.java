package util;

import java.util.UUID;

public class GenerateAuthToken {
    public static String generateAuthToken() {
        return UUID.randomUUID().toString();
    }
}
