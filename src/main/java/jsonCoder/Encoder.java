package jsonCoder;

import com.google.gson.Gson;

public class Encoder {
    public static String serialize(Object value) {
        return (new Gson()).toJson(value);
    }
}
