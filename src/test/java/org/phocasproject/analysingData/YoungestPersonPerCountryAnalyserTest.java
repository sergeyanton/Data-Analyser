package org.phocasproject.analysingData;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.phocasproject.objects.Person;

public class YoungestPersonPerCountryAnalyserTest {

  private YoungestPersonPerCountryAnalyser analyser;
  private final PrintStream originalOut = System.out;
  private ByteArrayOutputStream outputStream;

  @BeforeEach
  public void setUp() {
    analyser = new YoungestPersonPerCountryAnalyser();
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

    HashMap<String, ArrayList<Person>> expected = new HashMap<>();
    ArrayList<Person> people = new ArrayList<>();
    people.add(person);
    expected.put("New Zealand", people);

    assertEquals(expected, analyser.getYoungestPeople());
  }

  @Test
  public void testProcessLine_MultiplePersonsSameCountry_SameAge() {
    Person person1 = new Person("John Doe", "New Zealand", 30);
    Person person2 = new Person("Jane Doe", "New Zealand", 30);

    analyser.processLine(person1);
    analyser.processLine(person2);

    ArrayList<Person> expectedPeople = new ArrayList<>();
    expectedPeople.add(person1);
    expectedPeople.add(person2);

    assertIterableEquals(expectedPeople, analyser.getYoungestPeople().get("New Zealand"));
  }

  @Test
  public void testProcessLine_MultiplePersonsSameCountry_DifferentAges() {
    Person person1 = new Person("John Doe", "New Zealand", 30);
    Person person2 = new Person("Jane Doe", "New Zealand", 20);

    analyser.processLine(person1);
    analyser.processLine(person2);

    ArrayList<Person> expectedPeople = new ArrayList<>();

    // Since person2 is younger, person1 should be replaced.
    expectedPeople.add(person2);

    assertIterableEquals(expectedPeople, analyser.getYoungestPeople().get("New Zealand"));
  }

  @Test
  public void testProcessLine_MultipleCountries() {
    Person person1 = new Person("John Doe", "New Zealand", 30);
    Person person2 = new Person("Jane Doe", "Australia", 25);

    analyser.processLine(person1);
    analyser.processLine(person2);

    HashMap<String, ArrayList<Person>> expected = new HashMap<>();
    ArrayList<Person> nzPeople = new ArrayList<>();
    ArrayList<Person> ausPeople = new ArrayList<>();
    nzPeople.add(person1);
    ausPeople.add(person2);

    expected.put("New Zealand", nzPeople);
    expected.put("Australia", ausPeople);

    assertEquals(expected, analyser.getYoungestPeople());
  }

  @Test
  public void testOutputResults_SingleCountry() {
    Person person = new Person("John Doe", "New Zealand", 30);
    analyser.processLine(person);

    analyser.outputResults();

    assertTrue(outputStream.toString().contains("- John Doe (Age: 30)"));
  }

  @Test
  public void testOutputResults_MultipleCountries() {
    Person person1 = new Person("John Doe", "New Zealand", 30);
    Person person2 = new Person("Jane Doe", "Australia", 25);

    analyser.processLine(person1);
    analyser.processLine(person2);

    analyser.outputResults();

    assertTrue(outputStream.toString().contains("- John Doe (Age: 30)"));
    assertTrue(outputStream.toString().contains("- Jane Doe (Age: 25)"));
  }

  @Test
  public void testOutputResults_SameCountryMultipleYoungest() {
    Person person1 = new Person("John Doe", "New Zealand", 30);
    Person person2 = new Person("Jane Doe", "New Zealand", 30);

    analyser.processLine(person1);
    analyser.processLine(person2);

    analyser.outputResults();

    String expectedOutputNZ = "New Zealand\n - John Doe (Age: 30) \n - Jane Doe (Age: 30) \n";
    assertTrue(outputStream.toString().contains("- John Doe (Age: 30)"));
    assertTrue(outputStream.toString().contains("- Jane Doe (Age: 30)"));
  }
}
