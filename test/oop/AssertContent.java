package oop;

import org.junit.jupiter.api.Assertions;

import java.util.List;

public class AssertContent {

    public static boolean assertEqualDisordered(List<Message> expected, List<Message> actual) {
            // Check if size is different or some list don't contain the other
            if(expected.size() != actual.size() || !expected.stream().allMatch(msg -> actual.contains(msg))) {
                return false;
            }

            return true;
    }
}
