package org.phocasproject.analysingData;

import java.util.TreeMap;
import org.phocasproject.objects.Person;

/**
 * Analyser that calculates the number of people in each age range in New Zealand.
 */
public class AgeRangeInNZAnalyser implements Analyser {

  private final TreeMap<Integer, Integer> ageRangeCounter;

  /**
   * Constructor that initialises the age range counter.
   */
  public AgeRangeInNZAnalyser() {
    ageRangeCounter = new TreeMap<>();
  }

  /**
   * Processes a single line of data, adding the age range to the counter.
   *
   * @param personToProcess the person to process.
   */
  @Override
  public void processLine(Person personToProcess) {
    if (!personToProcess.getCountry().equals("New Zealand")) {
      return;
    }
    // Calculate the lower bound of the age range
    int ageRange = ((int) personToProcess.getAge() / 10) * 10;

    // Increment the count for the age range
    if (ageRangeCounter.containsKey(ageRange)) {
      ageRangeCounter.put(ageRange, ageRangeCounter.get(ageRange) + 1);
    } else {
      ageRangeCounter.put(ageRange, 1);
    }
  }

  /**
   * Outputs the number of people in each age range in New Zealand.
   */
  @Override
  public void outputResults() {
    System.out.println("\nThe number of people in each age range in New Zealand is: ");
    ageRangeCounter.forEach((ageRange, count) -> {
      System.out.printf("%d <= x < %d: %d\n", ageRange, ageRange + 10, count);
    });
  }

  // Getter for ageRangeCounter for testing purposes
  public TreeMap<Integer, Integer> getAgeRangeCounter() {
    return ageRangeCounter;
  }

}
