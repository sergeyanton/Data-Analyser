package org.phocasproject.analysingData;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.phocasproject.objects.Person;

public class AverageAgePerCountryAnalyserTest {

  private AverageAgePerCountryAnalyser analyser;
  private final PrintStream originalOut = System.out;
  private ByteArrayOutputStream outputStream;

  @BeforeEach
  public void setUp() {
    analyser = new AverageAgePerCountryAnalyser();
    outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);
    System.setOut(printStream);
  }

  @AfterEach
  public void tearDown() {
    System.setOut(originalOut);
  }

  @Test
  public void testProcessLine_SingleCountry() {
    Person person = new Person("John Doe", "New Zealand", 30);
    analyser.processLine(person);

    assertEquals(30.0, analyser.getTotalAgePerCountry().get("New Zealand"));
    assertEquals(1, analyser.getCountPerCountry().get("New Zealand"));
  }

  @Test
  public void testProcessLine_MultipleCountries() {
    analyser.processLine(new Person("John Doe", "New Zealand", 30));
    analyser.processLine(new Person("Jane Doe", "New Zealand", 40));
    analyser.processLine(new Person("Jim Doe", "Australia", 50));

    assertEquals(70.0, analyser.getTotalAgePerCountry().get("New Zealand"));
    assertEquals(50.0, analyser.getTotalAgePerCountry().get("Australia"));
    assertEquals(2, analyser.getCountPerCountry().get("New Zealand"));
    assertEquals(1, analyser.getCountPerCountry().get("Australia"));
  }

  @Test
  public void testOutputResults() {
    analyser.processLine(new Person("Jane Doe", "Australia", 40));
    analyser.processLine(new Person("Jim Doe", "New Zealand", 20));

    analyser.outputResults();

    assertTrue(outputStream.toString().contains("New Zealand: 20.0"));
    assertTrue(outputStream.toString().contains("Australia: 40.0"));
  }

  @Test
  public void testOutputResults_NoPeople() {
    analyser.outputResults();

    String expectedOutput = "Average age per country:";
    assertTrue(outputStream.toString().contains(expectedOutput));
  }

}
