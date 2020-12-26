package patterns;

import oop.AssertContent;
import oop.MailStore;
import oop.Message;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.UUID;

public class EncodeStrategyTest {
    private String[] bodies;
    private EncodeStrategy[] strategies;
    private ArrayList<String> testBodies, testBodies2;
    private ArrayList<String> expectedBodies;

    @BeforeEach
    void setUp() {
        strategies = new EncodeStrategy[]{
           new AESStrategy(),
           new ReverseStrategy(),
        };

        bodies = new String[] {
                "Body 1",
                "Body 2",
                "Another Body",
                "...",
                "Body bit larger... Little more...",
        };

        expectedBodies = new ArrayList<String>();
        for(String body: bodies) {
            expectedBodies.add(body);
        }
    }

    @Test
    void encodeAndDecode() {
        // Load data and encode/decode
        for(EncodeStrategy strategy: strategies) {
            // Encode and check that bodies are different
            this.testBodies = new ArrayList<String>();
            for(String body: bodies) {
                testBodies.add(strategy.encode(body));
            }

            Assertions.assertFalse(
                    AssertContent.assertEqualDisordered(expectedBodies, testBodies)
            );

            // Decode and check that bodies are equal
            testBodies2 = new ArrayList<String>();
            for(String body: testBodies) {
                testBodies2.add(strategy.decode(body));
            }

            Assertions.assertTrue(
                    AssertContent.assertEqualDisordered(expectedBodies, testBodies2)
            );
        }
    }
}
