import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * A program that performs a global search and replace operation on an input
 * text file
 * based on word replacement rules defined in another text file. The replacement
 * rules are stored
 * in a map data structure (BSTreeMap, RBTreeMap, or MyHashMap).
 *
 * Usage: java WordReplacer <input text file> <word replacements file>
 * <bst|rbt|hash>
 *
 * Author: [Your Name]
 */
public class WordReplacer {

    public static void main(String[] args) {
        // Check if the command-line arguments are valid
        if (args.length != 3) {
            System.err.println("Usage: java WordReplacer <input text file> <word replacements file> <bst|rbt|hash>");
            System.exit(1);
        }

        // Read input arguments
        String inputTextFile = args[0];
        String replacementsFile = args[1];
        String dataStructureType = args[2];

        // Validate if the input files exist
        if (!isFileValid(inputTextFile)) {
            System.err.println("Error: Cannot open file '" + inputTextFile + "' for input.");
            System.exit(1);
        }

        if (!isFileValid(replacementsFile)) {
            System.err.println("Error: Cannot open file '" + replacementsFile + "' for input.");
            System.exit(1);
        }

        // Create the appropriate data structure for replacements
        MyMap<String, String> replacementsMap = createDataStructure(dataStructureType);

        // Load replacement rules into the map
        try {
            loadReplacements(replacementsFile, replacementsMap);
        } catch (FileNotFoundException e) {
            System.err.println("File not found", e);
        }

        // Read the input text and replace words according to the map
        String modifiedText = processInputText(inputTextFile, replacementsMap);

        // Print the modified text to the console
        System.out.printf("%s\n", modifiedText);
    }

    /**
     * Checks if a file exists.
     * 
     * @param fileName the name of the file to check
     * @return true if the file exists, false otherwise
     */
    private static boolean isFileValid(String fileName) {
        java.io.File file = new java.io.File(fileName);
        return file.exists();
    }

    /**
     * Creates an instance of the specified data structure for storing word
     * replacements.
     * 
     * @param type the type of data structure to create ("bst", "rbt", or "hash")
     * @return an instance of MyMap<String, String>
     */
    private static MyMap<String, String> createDataStructure(String type) {
        switch (type.toLowerCase()) {
            case "bst":
                return new BSTreeMap<>();
            case "rbt":
                return new RBTreeMap<>();
            case "hash":
                return new MyHashMap<>();
            default:
                System.err.println("Error: Invalid data structure '" + type + "' received.");
                System.exit(1);
                return null;
        }
    }

    /**
     * Loads replacement rules from the specified file into the provided map.
     * Detects and handles cycles according to the problem's requirements.
     *
     * @param fileName        the name of the file containing the replacement rules
     * @param replacementsMap the map to store the replacements
     */
    private static void loadReplacements(String fileName, MyMap<String, String> replacementsMap) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;

            // Process each rule in the file
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("->");
                if (parts.length == 2) {
                    String key = parts[0].trim();
                    String value = parts[1].trim();

                    // Detect direct cycles
                    if (key.equals(value)) {
                        System.err.println(
                                "Error: Cycle detected when trying to add replacement rule: " + key + " -> " + value);
                        System.exit(1);
                    }

                    // Detect transitive cycles
                    if (createsCycle(replacementsMap, key, value)) {
                        System.err.println(
                                "Error: Cycle detected when trying to add replacement rule: " + key + " -> " + value);
                        System.exit(1);
                    }

                    // Add rule to the map
                    replacementsMap.put(key, value);
                }
            }
        } catch (IOException e) {
            System.err.println("Error: An I/O error occurred reading '" + fileName + "'.");
            System.exit(1);
        }
    }

    /**
     * Checks if adding the replacement rule key -> value would create a cycle.
     * The method traverses the replacement chain to detect cycles efficiently.
     *
     * @param map   the replacement map
     * @param key   the key in the rule
     * @param value the value in the rule
     * @return true if adding the rule creates a cycle, false otherwise
     */
    private static boolean createsCycle(MyMap<String, String> map, String key, String value) {
        String current = value;
        while (current != null) {
            if (current.equals(key)) {
                return true; // Cycle detected
            }
            current = map.get(current); // Follow the chain
        }
        return false;
    }

    /**
     * Processes the input text, replacing words according to the replacement map.
     * 
     * @param fileName        the name of the input text file
     * @param replacementsMap the map containing word replacements
     * @return the modified text as a string
     */
    private static String processInputText(String fileName, MyMap<String, String> replacementsMap) {
        StringBuilder modifiedText = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                StringBuilder currentWord = new StringBuilder();
                for (int i = 0; i < line.length(); i++) {
                    char ch = line.charAt(i);
                    if (Character.isLetter(ch)) {
                        currentWord.append(ch);
                    } else {
                        if (currentWord.length() > 0) {
                            String word = currentWord.toString();
                            String replacement = replacementsMap.get(word);
                            if (replacement != null) {
                                modifiedText.append(replacement);
                            } else {
                                modifiedText.append(word);
                            }
                            currentWord.setLength(0);
                        }
                        modifiedText.append(ch);
                    }
                }
                if (currentWord.length() > 0) {
                    String word = currentWord.toString();
                    String replacement = replacementsMap.get(word);
                    if (replacement != null) {
                        modifiedText.append(replacement);
                    } else {
                        modifiedText.append(word);
                    }
                }
                modifiedText.append("\n");
            }
        } catch (IOException e) {
            System.err.println("Error: An I/O error occurred reading '" + fileName + "'.");
            System.exit(1);
        }
        return modifiedText.toString();
    }
}
