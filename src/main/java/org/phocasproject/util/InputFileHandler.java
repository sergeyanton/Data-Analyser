package org.phocasproject.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import org.json.JSONObject;
import org.phocasproject.analysingData.Analyser;
import org.phocasproject.objects.Person;

/**
 * Class to handle reading an NDJSON file and parsing the data into Person objects.
 */
public class InputFileHandler {

  /**
   * Reads an NDJSON file and returns a list of Person objects.
   *
   * @param fileName The name of the NDJSON file to read.
   * @return A list of Person objects parsed from the file.
   * @throws IOException If there's an error reading the file.
   */
  public static ArrayList<Analyser> readPersonsFromFile(String fileName,
      ArrayList<Analyser> analysers) throws IOException {
    checkFileExists(fileName);
    try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
      String line;
      while ((line = reader.readLine()) != null) {
        Person person = parsePersonFromJson(line);
        if (person != null) {
          analysers.forEach(analyser -> analyser.processLine(person));
        }
      }
    } catch (IOException e) {
      System.err.println("Error reading file: " + e.getMessage());
    }
    return analysers;
  }


  /**
   * Parses a JSON string into a Person object.
   *
   * @param jsonString The JSON string to parse.
   * @return A Person object, or null if parsing fails.
   */
  private static Person parsePersonFromJson(String jsonString) {
    try {
      JSONObject json = new JSONObject(jsonString);

      // Check if all required fields are present
      if (!json.has("firstName") || !json.has("lastName") || !json.has("country") || !json.has(
          "age")) {
        System.err.println(
            "Error parsing JSON: Missing required fields. Be ware: integrity of data may be compromised");
        return null;
      }
      // Check too many fields
      if (json.keySet().size() > 4) {
        System.err.println(
            "Error parsing JSON: Too many fields. Be ware: integrity of data may be compromised");
        return null;
      }

      String firstName = json.getString("firstName");
      String lastName = json.getString("lastName");
      String fullName = firstName + " " + lastName;
      String country = json.getString("country");
      int age = json.getInt("age");

      // Validate the parsed data
      if (age < 0 || firstName.isBlank() || lastName.isBlank() || country.isBlank()) {
        System.err.println(
            "Error parsing JSON: Invalid data. Be ware: integrity of data may be compromised");
        return null;
      }

      return new Person(fullName, country, age);
    } catch (Exception e) {
      System.err.println("Error parsing JSON: " + e.getMessage());
      return null;
    }
  }

  /**
   * Checks if a file exists and is readable.
   *
   * @param fileName The name of the file to check.
   */
  private static void checkFileExists(String fileName) {
    File file = new File(fileName);
    if (!file.exists()) {
      throw new IllegalArgumentException("File does not exist: " + fileName);
    }
    if (!file.isFile()) {
      throw new IllegalArgumentException("Path is not a file: " + fileName);
    }
    if (!file.canRead()) {
      throw new IllegalArgumentException("File is not readable: " + fileName);
    }
  }

}
