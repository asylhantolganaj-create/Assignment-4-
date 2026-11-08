import java.util.*;

public class StudentTests {
    public static void main(String[] args) {
        testKmpEmptyInputs();
        testLcsSimple();
        testNaiveOverlap();
        System.out.println("All student tests passed!");
    }

    static void testKmpEmptyInputs() {
        Map<String, Object> input = new HashMap<>();
        input.put("text", "");
        input.put("pattern", "");
        Map<String, Object> result = Algorithms.kmp(input);
        List<Integer> expected = Arrays.asList(0);
        if (!expected.equals(result.get("positions"))) {
            throw new RuntimeException("KMP empty test failed");
        }
    }

    static void testLcsSimple() {
        Map<String, Object> input = new HashMap<>();
        input.put("a", "abc");
        input.put("b", "ac");
        Map<String, Object> result = Algorithms.lcs(input);
        if (!result.get("length").equals(2) || !result.get("sequence").equals("ac")) {
            throw new RuntimeException("LCS simple test failed");
        }
    }

    static void testNaiveOverlap() {
        Map<String, Object> input = new HashMap<>();
        input.put("text", "aaaaa");
        input.put("pattern", "aaa");
        Map<String, Object> result = Algorithms.naive(input);
        List<Integer> expected = Arrays.asList(0, 1, 2);
        if (!expected.equals(result.get("positions"))) {
            throw new RuntimeException("Naive overlap test failed");
        }
    }
}