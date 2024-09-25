package org.phocasproject.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.phocasproject.analysingData.Analyser;
import org.phocasproject.objects.Person;

public class InputFileHandlerTest {

  @TempDir
  Path tempDir;

  private Analyser mockAnalyser;
  private ArrayList<Analyser> analysers;

  // Helper method to create a temporary file with the given name and content.
  private File createTempFile(String fileName, String content) throws IOException {
    File tempFile = tempDir.resolve(fileName).toFile();
    try (FileWriter writer = new FileWriter(tempFile)) {
      writer.write(content);
    }
    return tempFile;
  }

  @BeforeEach
  void setUp() {
    mockAnalyser = mock(Analyser.class);
    analysers = new ArrayList<>(List.of(mockAnalyser));
  }

  @Test
  void testReadPersonsFromFile_FileIsValid() throws IOException {
    File tempFile = createTempFile("valid.ndjson",
        """
            {"firstName":"John","lastName":"Doe","country":"USA","age":30}
            {"firstName":"Jane","lastName":"Smith","country":"UK","age":25}""");

    ArrayList<Analyser> result = InputFileHandler.readPersonsFromFile(tempFile.getAbsolutePath(),
        analysers);

    assertEquals(1, result.size());
    assertTrue(result.contains(mockAnalyser));

    ArgumentCaptor<Person> personCaptor = ArgumentCaptor.forClass(Person.class);
    verify(mockAnalyser, times(2)).processLine(personCaptor.capture());

    List<Person> capturedPersons = personCaptor.getAllValues();

    assertEquals("John Doe", capturedPersons.get(0).getName());
    assertEquals("USA", capturedPersons.get(0).getCountry());
    assertEquals(30, capturedPersons.get(0).getAge());

    assertEquals("Jane Smith", capturedPersons.get(1).getName());
    assertEquals("UK", capturedPersons.get(1).getCountry());
    assertEquals(25, capturedPersons.get(1).getAge());

  }

  @Test
  void testReadPersonsFromFile_FileIsEmpty() throws IOException {
    File tempFile = createTempFile("empty.ndjson", "");

    ArrayList<Analyser> result = InputFileHandler.readPersonsFromFile(tempFile.getAbsolutePath(),
        analysers);

    assertEquals(1, result.size());
    assertTrue(result.contains(mockAnalyser));

    ArgumentCaptor<Person> personCaptor = ArgumentCaptor.forClass(Person.class);
    verify(mockAnalyser, times(0)).processLine(personCaptor.capture());

    List<Person> capturedPersons = personCaptor.getAllValues();
    assertTrue(capturedPersons.isEmpty());
  }

  @Test
  void testReadPersonsFromFile_FileDoesNotExist() {
    String nonExistentFile = tempDir.resolve("non_existent.ndjson").toString();

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> InputFileHandler.readPersonsFromFile(nonExistentFile, new ArrayList<>())
    );
    assertTrue(exception.getMessage().contains("File does not exist"));
  }

  @Test
  void testReadPersonsFromFile_FileIsDirectory() throws IOException {
    File directory = tempDir.resolve("directory").toFile();
    directory.mkdir();

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> InputFileHandler.readPersonsFromFile(directory.getAbsolutePath(), new ArrayList<>())
    );
    assertTrue(exception.getMessage().contains("Path is not a file"));
  }

  @Test
  void testReadPersonsFromFile_FileNotReadable() throws IOException {
    File tempFile = createTempFile("unreadable.ndjson", "{}");
    boolean permission = tempFile.setReadable(false, true);

    // If it is not possible to set the file to be unreadable due to permissions then skip the test
    if (!permission) {
      return;
    }

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> InputFileHandler.readPersonsFromFile(tempFile.getAbsolutePath(), new ArrayList<>())
    );
    assertTrue(exception.getMessage().contains("File is not readable"));
  }


  @Test
  void testReadPersonsFromFile_FileDoesntExist() {
    String nonExistentFile = tempDir.resolve("non_existent.ndjson").toString();

    assertThrows(IllegalArgumentException.class, () ->
        InputFileHandler.readPersonsFromFile(nonExistentFile, analysers)
    );
  }

  /**
   * Provides invalid person data for testing.
   *
   * @return A stream of invalid person data.
   */
  private static Stream<String> provideInvalidPersonData() {
    return Stream.of(
        "{\"firstName\":\"\",\"lastName\":\"David\",\"country\":\"USA\",\"age\":33}",
        // Missing first name
        "{\"firstName\":\"Jacob\",\"lastName\":\"\",\"country\":\"USA\",\"age\":33}",
        // Missing last name
        "{\"firstName\":\"Jacob\",\"lastName\":\"David\",\"country\":\"\",\"age\":33}",
        // Missing country
        "{\"firstName\":\"Jacob\",\"lastName\":\"David\",\"country\":\"USA\",\"age\":}",
        // Missing age
        "{\"firstName\":\"Jacob\",\"lastName\":\"David\",\"country\":\"USA\",\"age\":xx}",
        // Invalid age
        "{\"firstName\":\"Jacob\",\"lastName\":\"David\",\"country\":\"USA\",\"age\":-1}",
        // Negative age
        "{\"firstName\":\"Jacob\",\"lastName\":\"David\",\"country\":\"USA\",\"age\":33, \"extra\":\"field\"}",
        // Extra field
        "{\"firstName\":\"Jacob\",\"lastName\":\"David\",\"country\":\"USA\"}", // Missing age field
        "{\"firstName\":\"Jacob\",\"lastName\":\"David\",\"age\":33}", // Missing country field
        "{\"firstName\":\"Jacob\",\"country\":\"USA\",\"age\":33}", // Missing last name field
        "{\"lastName\":\"David\",\"country\":\"USA\",\"age\":33}", // Missing first name field
        "{invalid json}" // Invalid JSON

    );
  }

  //   Parameterized test that checks if invalid data is ignored when reading from a file
  @ParameterizedTest
  @MethodSource("provideInvalidPersonData")
  void testReadPersonsFromFile_InvalidData(String invalidJson) throws IOException {
    String validJson1 = "{\"firstName\":\"Correct\",\"lastName\":\"Lad\",\"country\":\"Netherlands\",\"age\":30}";

    String fileContent = String.join("\n", validJson1, invalidJson);
    File tempFile = createTempFile("invalid.ndjson", fileContent);

    ArrayList<Analyser> results = InputFileHandler.readPersonsFromFile(tempFile.getAbsolutePath(),
        analysers);

    assertEquals(1, results.size());
    assertTrue(results.contains(mockAnalyser));

    ArgumentCaptor<Person> personCaptor = ArgumentCaptor.forClass(Person.class);
    verify(mockAnalyser, times(1)).processLine(personCaptor.capture());

    List<Person> capturedPersons = personCaptor.getAllValues();

    assertEquals("Correct Lad", capturedPersons.get(0).getName());
    assertEquals("Netherlands", capturedPersons.get(0).getCountry());
    assertEquals(30, capturedPersons.get(0).getAge());
  }
}
