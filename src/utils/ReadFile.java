package utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ReadFile {
    public ReadFile () {}

    public void readFile (String route) {
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader(route));
            String line = reader.readLine(); // Read the first line
            while (line != null) {
                System.out.println(line); // Print the line to the console
                line = reader.readLine(); // Read the next line
            }
        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        } finally {
            if (reader != null) {
                try {
                    reader.close(); // Close the reader to free resources
                } catch (IOException e) {
                    System.out.println("Error closing the file: " + e.getMessage());
                }
            }
        }

    }
}
