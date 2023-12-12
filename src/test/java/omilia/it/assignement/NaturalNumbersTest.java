package omilia.it.assignement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;

public class NaturalNumbersTest extends TestCase {
    public final String NUMBER_INVALID_1 = "30 2 5 58";
    public final String NUMBER_INVALID_NO_SPACE_1 = "302558";

    public final String NUMBER_INVALID_2 = "2 10 69 30 6 60 4";
    public final String NUMBER_INVALID_NO_SPACE_2 = "21069306604";

    public final String NUMBER_VALID_1 = "2 10 69 30 6 6 4";
    public final String NUMBER_VALID_NO_SPACE_1 = "2106930664";

    public final String NUMBER_VALID_2 = "0 0 30 69 74 0 9 22 52";
    public final String NUMBER_VALID_NO_SPACE_2 = "00306974092252";

    public void testdeleteSpaceInvalid() {
        assertEquals(NaturalNumbers.deleteSpace(NUMBER_INVALID_1), NUMBER_INVALID_NO_SPACE_1);
    }

    public void testInvalidGreekNumber() {
        assertFalse("This Number should NOT be valid", NaturalNumbers.isGreekNumber(NUMBER_INVALID_NO_SPACE_1));
    }

    public void testValidGreekNumber() {
        assertTrue("This Number should be valid", NaturalNumbers.isGreekNumber(NUMBER_VALID_NO_SPACE_1));
    }

    public void testSanitizeTooMuchSpaces() {
        assertEquals(NaturalNumbers.sanitize("  123    456  7  "), "123 456 7");
    }

    public void testValidInput() {
        try {
            NaturalNumbers.validateInput(NUMBER_VALID_2);
        } catch (IOException e) {
            fail();
        }
    }

    public void testInValidInputTooMuchDigits() {
        try {
            NaturalNumbers.validateInput("1234 5 6");
            fail();
        } catch (IOException e) {
            assertEquals("The number 1234 is up to 3 digits", e.getMessage());
        }
    }

    public void testInValidInputChar() {
        try {
            NaturalNumbers.validateInput("12C 5 6");
            fail();
        } catch (IOException e) {
            assertEquals("The group 12C is not a number", e.getMessage());
        }
    }

    public void testNotAmbiguousTenthToConcatenate() {
        assertFalse("This sequence is not an ambiguous case to concatenate",
                NaturalNumbers.isAmbiguousTenthToConcatenate("20", "12", "2"));
    }

    public void testAmbiguousTenthToConcatenate() {
        assertTrue("This sequence is an ambiguous case to concatenate",
                NaturalNumbers.isAmbiguousTenthToConcatenate("20", "2", "2"));
    }

    public void testNotAmbiguousTenthToSplit() {
        assertFalse("This sequence is not an ambiguous case to split",
                NaturalNumbers.isAmbiguousTenthToSplit("30", "3"));
    }

    public void testAmbiguousTenthToSplit() {
        assertTrue("This sequence is an ambiguous case to split",
                NaturalNumbers.isAmbiguousTenthToSplit("35", "3"));
    }

    public void testisAmbiguousHundred() {
        assertTrue("This sequence is an ambiguous hundred case to concatenate",
                NaturalNumbers.isAmbiguousHundredToConcatenate("700", "6", "7"));
    }

    public void testisAmbiguousHundredToConcatenate() {
        assertTrue("This sequence is an ambiguous hundred case to concatenate",
                NaturalNumbers.isAmbiguousHundredToConcatenate("700", "22", "7"));
    }

    public void testNotAmbiguousHundredToConcatenate() {
        assertFalse("This sequence is not an ambiguous hundred case to concatenate",
                NaturalNumbers.isAmbiguousHundredToConcatenate("700", "200", "7"));
    }

    public void testisAmbiguousHundredToSplit() {
        assertTrue("This sequence is an ambiguous hundred case to split",
                NaturalNumbers.isAmbiguousHundredToSplit("745", "7"));
    }

    public void testNotAmbiguousHundredToSplit() {
        assertFalse("This sequence is not ambiguous hundred case to split",
                NaturalNumbers.isAmbiguousHundredToSplit("700", "7"));
    }

    public void testgetOutputValidNumber() {
        assertEquals(NaturalNumbers.getOutputNumber(NUMBER_VALID_2),
                NUMBER_VALID_NO_SPACE_2 + "\t[phone number: VALID]");
    }

    public void testgetOutputInvalidNumber() {
        assertEquals(NaturalNumbers.getOutputNumber(NUMBER_INVALID_2),
                NUMBER_INVALID_NO_SPACE_2 + "\t[phone number: INVALID]");
    }

    public void testgenerateInterpretationsr() {
        ArrayList<String> list1 = new ArrayList<>(Arrays.asList("a", "b", "c"));
        ArrayList<String> list2 = new ArrayList<>(Arrays.asList("d"));
        ArrayList<String> list3 = new ArrayList<>(Arrays.asList("e", "f"));

        List<ArrayList<String>> interpretationsTree = new ArrayList<ArrayList<String>>();

        interpretationsTree.add(list1);
        interpretationsTree.add(list2);
        interpretationsTree.add(list3);

        ArrayList<String> result = new ArrayList<>(
                Arrays.asList(" a d e", " a d f", " b d e", " b d f", " c d e", " c d f"));

        assertEquals(NaturalNumbers.generateInterpretations(interpretationsTree), result);
    }

    public void testgetInterpretationsTree() {
        List<ArrayList<String>> tree = NaturalNumbers.getInterpretationsTree(NUMBER_INVALID_2);

        System.out.println(tree);

        List<ArrayList<String>> resultTree = new ArrayList<ArrayList<String>>();

        ArrayList<String> array1 = new ArrayList<>(Arrays.asList("2"));
        resultTree.add(array1);
        ArrayList<String> array2 = new ArrayList<>(Arrays.asList("10"));
        resultTree.add(array2);
        ArrayList<String> array3 = new ArrayList<>(Arrays.asList("69", "60 9"));
        resultTree.add(array3);
        ArrayList<String> array4 = new ArrayList<>(Arrays.asList("36", "30 6"));
        resultTree.add(array4);
        ArrayList<String> array5 = new ArrayList<>(Arrays.asList("64", "60 4"));
        resultTree.add(array5);

        assertEquals(tree, resultTree);
    }
}
