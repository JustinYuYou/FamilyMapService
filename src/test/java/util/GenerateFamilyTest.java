package util;

import model.User;
import org.graalvm.compiler.replacements.Log;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GenerateFamilyTest {

    GenerateFamily familyGenerator;

    User user = new User("sheila", "Sheila_Parker" ,"parker"
            ,"sheila@parker.com", "Sheila", "Parker", "f");
    @BeforeEach
    void setUp() {
        familyGenerator = new GenerateFamily(user);
    }
    @Test
    void startGenerating() {
        familyGenerator.startGenerating(3);
    }

    @Test
    void generateFamily() {
    }

    @Test
    void createRoot() {
    }
}