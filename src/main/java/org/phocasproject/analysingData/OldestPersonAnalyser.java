package org.phocasproject.analysingData;

import java.util.ArrayList;
import org.phocasproject.objects.Person;

/**
 * Analyser that finds the oldest person in the data.
 */
public class OldestPersonAnalyser implements Analyser {

  private final ArrayList<Person> oldestPerson;

  /**
   * Constructor that initialises the oldest person list.
   */
  public OldestPersonAnalyser() {
    oldestPerson = new ArrayList<>();
  }

  /**
   * Processes a single line of data, updating the oldest person list if necessary.
   *
   * @param personToProcess the person to process.
   */
  @Override
  public void processLine(Person personToProcess) {
    // If the oldestPerson list is empty, add the person to the list
    if (oldestPerson.isEmpty()) {
      oldestPerson.add(personToProcess);

    } else {
      // If the person is older than the current oldest person, clear the list and add the new person
      Person currentOldestPerson = oldestPerson.get(0);

      if (personToProcess.getAge() > currentOldestPerson.getAge()) {
        oldestPerson.clear();
        oldestPerson.add(personToProcess);

      } else if (personToProcess.getAge() == currentOldestPerson.getAge()) {
        // If the person is the same age as the current oldest person, add them to the list
        oldestPerson.add(personToProcess);
      }
    }
  }

  /**
   * Outputs the oldest person in the data.
   */
  @Override
  public void outputResults() {
    System.out.println("\nThe oldest person/people are: ");
    oldestPerson.forEach(
        person -> System.out.printf("- %s", person.toString()));
  }

  // Getter for oldestPerson for testing purposes
  public ArrayList<Person> getOldestPerson() {
    return oldestPerson;
  }
}
