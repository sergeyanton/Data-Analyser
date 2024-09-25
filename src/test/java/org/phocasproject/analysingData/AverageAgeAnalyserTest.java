package org.phocasproject.analysingData;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.phocasproject.objects.Person;

public class AverageAgeAnalyserTest {

  private AverageAgeAnalyser analyser;
  private final PrintStream originalOut = System.out;
  private ByteArrayOutputStream outputStream;

  @BeforeEach
  public void setUp() {
    analyser = new AverageAgeAnalyser();
    outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);
    System.setOut(printStream);
  }

  @AfterEach
  public void tearDown() {
    System.setOut(originalOut);
  }


  @Test
  public void testProcessLine_SinglePerson() {
    Person person = new Person("John Doe", "New Zealand", 30);
    analyser.processLine(person);

    assertEquals(30.0, analyser.getTotalAge());
    assertEquals(1, analyser.getNumberOfPeople());
  }

  @Test
  public void testProcessLine_MultiplePersons() {
    analyser.processLine(new Person("John Doe", "New Zealand", 30));
    analyser.processLine(new Person("Jane Doe", "New Zealand", 40));
    analyser.processLine(new Person("Jim Doe", "New Zealand", 50));

    assertEquals(120.0, analyser.getTotalAge());
    assertEquals(3, analyser.getNumberOfPeople());
  }

  @Test
  public void testOutputResults() {
    analyser.processLine(new Person("John Doe", "New Zealand", 30));
    analyser.processLine(new Person("Jane Doe", "New Zealand", 40));
    analyser.processLine(new Person("Jim Doe", "New Zealand", 50));

    analyser.outputResults();

    String expectedOutput = "\nThe average age of all people is: 40.0 \n";
    assertTrue(outputStream.toString().contains(expectedOutput));
  }

  @Test
  public void testOutputResults_NoPeople() {
    analyser.outputResults();

    String expectedOutput = "The average age of all people is: NaN";
    assertTrue(outputStream.toString().contains(expectedOutput));
  }
}
