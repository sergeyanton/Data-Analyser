package org.phocasproject.analysingData;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.phocasproject.objects.Person;

class AgeRangeInNZAnalyserTest {

  private AgeRangeInNZAnalyser analyser;
  private final PrintStream originalOut = System.out;

  @BeforeEach
  public void setUp() {
    analyser = new AgeRangeInNZAnalyser();
  }

  @Test
  public void testProcessLine_WithPersonInNZ() {
    Person person = new Person("John Doe", "New Zealand", 29);
    analyser.processLine(person);
    assertEquals(1, analyser.getAgeRangeCounter().get(20));
  }

  @Test
  public void testProcessLine_WithPersonOutsideNZ() {
    Person person = new Person("John Doe", "Australia", 29);
    analyser.processLine(person);
    assertNull(analyser.getAgeRangeCounter().get(30));
  }

  @Test
  public void testMultiplePersonsInSameAgeRange() {
    analyser.processLine(new Person("John Doe", "New Zealand", 29));
    analyser.processLine(new Person("Jane Doe", "New Zealand", 25));
    analyser.processLine(new Person("Jim Doe", "New Zealand", 20));

    assertEquals(3, analyser.getAgeRangeCounter().get(20));
  }

  @Test
  public void testOutputResults() {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);
    System.setOut(printStream);

    analyser.processLine(new Person("John Doe", "New Zealand", 5));
    analyser.processLine(new Person("Jane Doe", "New Zealand", 25));
    analyser.processLine(new Person("Jim Doe", "New Zealand", 35));

    analyser.outputResults();

    System.setOut(originalOut);

    assertTrue(outputStream.toString().contains("0 <= x < 10: 1"));
    assertTrue(outputStream.toString().contains("20 <= x < 30: 1"));
    assertTrue(outputStream.toString().contains("30 <= x < 40: 1"));
  }


}