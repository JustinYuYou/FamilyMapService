package util;

import java.util.UUID;

public class GenerateRandom {
    public static String generateRandomString() {
        return UUID.randomUUID().toString();
    }
}
