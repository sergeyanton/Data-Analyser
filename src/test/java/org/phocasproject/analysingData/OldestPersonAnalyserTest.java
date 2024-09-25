package org.phocasproject.analysingData;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.phocasproject.objects.Person;

public class OldestPersonAnalyserTest {

  private OldestPersonAnalyser analyser;
  private final PrintStream originalOut = System.out;
  private ByteArrayOutputStream outputStream;

  @BeforeEach
  public void setUp() {
    analyser = new OldestPersonAnalyser();
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

    ArrayList<Person> expected = new ArrayList<>();
    expected.add(person);

    assertIterableEquals(expected, analyser.getOldestPerson());
  }

  @Test
  public void testProcessLine_MultiplePersons_SameAge() {
    Person person1 = new Person("John Doe", "New Zealand", 50);
    Person person2 = new Person("Jane Doe", "New Zealand", 50);

    analyser.processLine(person1);
    analyser.processLine(person2);

    ArrayList<Person> expected = new ArrayList<>();
    expected.add(person1);
    expected.add(person2);

    assertIterableEquals(expected, analyser.getOldestPerson());
  }

  @Test
  public void testProcessLine_MultiplePersons_DifferentAges() {
    Person person1 = new Person("John Doe", "New Zealand", 50);
    Person person2 = new Person("Jane Doe", "New Zealand", 40);
    Person person3 = new Person("Jim Doe", "New Zealand", 60);

    analyser.processLine(person1);
    analyser.processLine(person2);
    analyser.processLine(person3);

    ArrayList<Person> expected = new ArrayList<>();
    expected.add(person3);

    assertIterableEquals(expected, analyser.getOldestPerson());
  }

  @Test
  public void testProcessLine_ReplaceOldestPerson() {
    Person person1 = new Person("John Doe", "New Zealand", 40);
    Person person2 = new Person("Jim Doe", "New Zealand", 70);

    analyser.processLine(person1);
    analyser.processLine(person2);

    ArrayList<Person> expected = new ArrayList<>(List.of(person2));

    assertIterableEquals(expected, analyser.getOldestPerson());
  }

  @Test
  public void testOutputResults_SingleOldest() {
    Person person = new Person("John Doe", "New Zealand", 50);
    analyser.processLine(person);

    analyser.outputResults();

    String expectedOutput = person.toString();
    assertTrue(outputStream.toString().contains(expectedOutput));
  }

  @Test
  public void testOutputResults_MultipleOldest() {
    Person person1 = new Person("John Doe", "New Zealand", 50);
    Person person2 = new Person("Jane Doe", "New Zealand", 50);
    analyser.processLine(person1);
    analyser.processLine(person2);

    analyser.outputResults();

    String expectedOutput = person1.toString();
    assertTrue(outputStream.toString().contains(expectedOutput));

    expectedOutput = person2.toString();
    assertTrue(outputStream.toString().contains(expectedOutput));
  }

  @Test
  public void testOutputResults_NoPeople() {
    analyser.outputResults();
    String expectedOutput = "\nThe oldest person/people are: ";
    assertTrue(outputStream.toString().contains(expectedOutput));
  }
}
