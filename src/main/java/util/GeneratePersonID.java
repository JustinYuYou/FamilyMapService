package util;

import java.util.UUID;

public class GeneratePersonID {
    public static String generatePersonID() {
        return UUID.randomUUID().toString();
    }
}
