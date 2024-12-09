
import utils.ReadFile;

import java.io.File;

public class WordReplacerMain {

    public static void main(String[] args) {
        // Check if the number of arguments is correct
        if (args.length != 3) {
            System.err.println("Usage: java WordReplacer <input text file> <word replacements file> <bst|rbt|hash>");
            System.exit(1);
        }

        // Check if the input text file exists
        String inputTextFile = args[0];
        File inputFile = new File(inputTextFile);
        if (!inputFile.exists()) {
            System.err.println("Error: Cannot open file '" + inputTextFile + "' for input.");
            System.exit(1);
        }

        // Check if the word replacements file exists
        String wordReplacementsFile = args[1];
        File replacementsFile = new File(wordReplacementsFile);
        if (!replacementsFile.exists()) {
            System.err.println("Error: Cannot open file '" + wordReplacementsFile + "' for input.");
            System.exit(1);
        }

        // Check if the third argument is valid
        String dataStructure = args[2];
        if (!dataStructure.equalsIgnoreCase("bst") &&
                !dataStructure.equalsIgnoreCase("rbt") &&
                !dataStructure.equalsIgnoreCase("hash")) {
            System.err.println("Error: Invalid data structure '" + dataStructure + "' received.");
            System.exit(1);
        }

        // Print a message to show validation succeeded
        ReadFile readFile = new ReadFile();
        readFile.readFile(args[0]);
        readFile.readFile(args[1]);
    }
}
