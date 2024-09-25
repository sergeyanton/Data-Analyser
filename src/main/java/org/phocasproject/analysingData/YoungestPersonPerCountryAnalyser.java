package org.phocasproject.analysingData;

import java.util.ArrayList;
import java.util.HashMap;
import org.phocasproject.objects.Person;

/**
 * Analyser that finds the youngest person in each country.
 */
public class YoungestPersonPerCountryAnalyser implements Analyser {

  private final HashMap<String, ArrayList<Person>> youngestPeople;

  /**
   * Constructor that initialises the youngest people list.
   */
  public YoungestPersonPerCountryAnalyser() {
    youngestPeople = new HashMap<>();
  }

  /**
   * Processes a single line of data, updating the youngest person list if necessary.
   *
   * @param personToProcess the person to process.
   */
  @Override
  public void processLine(Person personToProcess) {
    String country = personToProcess.getCountry();
    // If the youngestPeople list does not contain the country add the person to the list
    if (!youngestPeople.containsKey(country)) {
      ArrayList<Person> people = new ArrayList<>();
      people.add(personToProcess);
      youngestPeople.put(country, people);

    } else {
      // If the person is younger than the current youngest person clear the list and add the new
      ArrayList<Person> people = youngestPeople.get(country);
      if (people.get(0).getAge() > personToProcess.getAge()) {
        people.clear();
        people.add(personToProcess);

      } else if (people.get(0).getAge() == personToProcess.getAge()) {
        // If the person is the same age as the current youngest person, add them to the list
        people.add(personToProcess);
      }
    }
  }

  /**
   * Outputs the youngest person in each country.
   */
  @Override
  public void outputResults() {
    System.out.println("\nThe youngest person/people in each country is:");
    youngestPeople.forEach((country, people) -> {
      System.out.println(country);
      people.forEach(person -> {
        System.out.printf(" - %s (Age: %d) \n", person.getName(), (int) person.getAge());
      });
    });
  }

  // Getter for youngestPeople for testing purposes
  public HashMap<String, ArrayList<Person>> getYoungestPeople() {
    return youngestPeople;
  }
}
