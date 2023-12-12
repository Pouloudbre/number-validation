package omilia.it.assignement;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class NaturalNumbers {

    public static String prompt() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        return reader.readLine();
    }

    public static String deleteSpace(String phoneNumber) {
        return phoneNumber.replaceAll(" ", "");
    }

    public static void validateInput(String phoneNumber) throws IOException {
        String[] splittedNumbers = phoneNumber.split(" ");
        for (String number : splittedNumbers) {
            if (number.length() > 3) {
                throw new IOException("The number " + number + " is up to 3 digits");
            }
            if (!number.matches("[0-9]+")) {
                throw new IOException("The group " + number + " is not a number");
            }
        }
    }

    public static String sanitize(String phoneNumber) {
        return phoneNumber.trim() // for edge case of white space before and after
                .replaceAll(" +", " "); // for edge case of many white space
    }

    public static boolean isGreekNumber(String numberdeleteSpace) {
        if (numberdeleteSpace.length() == 10) {
            return numberdeleteSpace.substring(0, 2).equals("69") || numberdeleteSpace.substring(0, 1).equals("2");
        } else if (numberdeleteSpace.length() == 14) {
            return numberdeleteSpace.substring(0, 6).equals("003069")
                    || numberdeleteSpace.substring(0, 5).equals("00302");
        }
        return false;
    }

    // This method return true in the ambiguous case like 20 8
    public static boolean isAmbiguousTenthToConcatenate(String firstGroup, String secondGroup, String firstChar) {
        String lastChar = firstGroup.substring(firstGroup.length() - 1);
        return lastChar.equals("0") && secondGroup.length() == 1 && !firstChar.equals("1") && !firstChar.equals("0");
    }

    // This method return true in the ambiguous case like 28
    public static boolean isAmbiguousTenthToSplit(String firstGroup, String firstChar) {
        String lastChar = firstGroup.substring(firstGroup.length() - 1);
        return !lastChar.equals("0") && !firstChar.equals("1") && !firstChar.equals("0");
    }

    // This method return true in the ambiguous case like 700 22
    public static boolean isAmbiguousHundredToConcatenate(String firstGroup, String secondGroup, String firstChar) {
        String lastChars = firstGroup.substring(1, firstGroup.length());
        return lastChars.equals("00") && secondGroup.length() < 3 && !firstChar.equals("0");
    }

    public static boolean isAmbiguousHundredToSplit(String firstGroup, String firstChar) {
        String lastChars = firstGroup.substring(1, firstGroup.length());
        // String secondChar = firstGroup.substring(1, firstGroup.length()-1);
        return !lastChars.equals("00") && !firstChar.equals("0");
    }

    public static String getOutputNumber(String phoneNumber) {
        phoneNumber = deleteSpace(phoneNumber);
        return (phoneNumber + "\t[phone number: " + (isGreekNumber(phoneNumber) ? "VALID" : "INVALID") + "]");
    }

    public static void printOutput(ArrayList<String> interpretations) {
        for (int i = 0; i < (interpretations).size(); i++) {
            System.out.println("Interpretation " + (i + 1) + ": " + getOutputNumber(interpretations.get(i)));
        }
    }

    // The idea of this foncion is that it gives an ArrayList of ArrayList of
    // strings with all possible interpretations located at the same index
    // eg: for "726 12" the ambiguous part is "726" so we should get :
    // [[700 26, 700 20 6, 726],[12]]
    public static List<ArrayList<String>> getInterpretationsTree(String phoneNumber) {
        String[] splittedNumbers = phoneNumber.split(" ");
        List<ArrayList<String>> interpretationsTree = new ArrayList<ArrayList<String>>();

        for (int i = 0; i < splittedNumbers.length; i++) {
            String currentGroup = splittedNumbers[i];
            String nextGroup = (i < splittedNumbers.length - 1) ? splittedNumbers[i + 1] : "000"; // to avoid out of
                                                                                                  // bond
            String firstChar = currentGroup.substring(0, 1);
            String lastChar = currentGroup.substring(currentGroup.length() - 1);
            ArrayList<String> options = new ArrayList<String>();

            // This block is for hundreds
            if (currentGroup.length() == 3) {
                String secondNextGroup = (i < splittedNumbers.length - 2) ? splittedNumbers[i + 2] : "00";
                if (isAmbiguousHundredToConcatenate(currentGroup, nextGroup, firstChar)) {
                    ArrayList<String> nextGroups = getInterpretationsTree(nextGroup + " " + secondNextGroup).get(0);
                    nextGroups.forEach(option -> {
                        options.add(currentGroup + " " + option);
                    });
                    if (nextGroups.get(0).length() == 1) {
                        options.add(currentGroup.charAt(0) + "0" + nextGroups.get(0));
                    } else {
                        options.add(currentGroup.charAt(0) + nextGroups.get(0));
                    }

                    i = i + 1;

                } else if (isAmbiguousHundredToSplit(currentGroup, firstChar)) {
                    // We need to differenciate if a unit or tenth follow
                    int sizeToCut = (String.valueOf(currentGroup.charAt(1)).equals("0")) ? 2 : 1;
                    List<ArrayList<String>> subInterpretationsTree = getInterpretationsTree(
                            currentGroup.charAt(0) + "00 " + currentGroup.substring(sizeToCut, currentGroup.length()));
                    options.addAll(subInterpretationsTree.get(0));
                } else {
                    options.add(currentGroup);
                }

            } else if (currentGroup.length() == 2) { // This block is for tenths
                if (isAmbiguousTenthToConcatenate(currentGroup, nextGroup, firstChar)) {
                    String newOption = currentGroup.charAt(0) + nextGroup;
                    options.add(newOption);
                    options.add(currentGroup + " " + nextGroup);
                    i++;
                } else if (isAmbiguousTenthToSplit(currentGroup, firstChar)) {
                    String newOption = currentGroup.charAt(0) + "0 " + lastChar;
                    options.add(currentGroup);
                    options.add(newOption);

                } else {
                    options.add(currentGroup);
                }
            } else {
                options.add(currentGroup);
            }
            interpretationsTree.add(options);
        }
        return interpretationsTree;
    }

    public static void generateCombination(List<ArrayList<String>> interpretationsTree, List<String> current,
            ArrayList<String> results) {
        if (current.size() >= interpretationsTree.size()) {
            String result = "";
            for (String group : current) {
                result = result + " " + group;
            }
            results.add(result);
            return;
        }
        int currentIndex = current.size();
        for (String group : interpretationsTree.get(currentIndex)) {
            current.add(group);
            generateCombination(interpretationsTree, current, results);
            current.remove(currentIndex);
        }
    }

    public static ArrayList<String> generateInterpretations(List<ArrayList<String>> interpretationsTree) {
        ArrayList<String> results = new ArrayList<>();
        generateCombination(interpretationsTree, new ArrayList<String>(), results);
        return results;
    }

    public static void main(String[] args) {
        while (true) {
            System.out.print("Type input: ");
            try {
                String phoneNumber = sanitize(prompt());
                validateInput(phoneNumber);
                List<ArrayList<String>> interpretationsTree = getInterpretationsTree(phoneNumber);
                ArrayList<String> interpretations = (generateInterpretations(interpretationsTree));
                printOutput(interpretations);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
