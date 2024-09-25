package org.phocasproject.analysingData;

import java.util.HashMap;
import org.phocasproject.objects.Person;

/**
 * Analyser that calculates the average age of all people.
 */
public class AverageAgePerCountryAnalyser implements Analyser {

  private final HashMap<String, Double> totalAgePerCountry;
  private final HashMap<String, Integer> countPerCountry;

  /**
   * Constructor that initialises the total age per country and the count per country.
   */
  public AverageAgePerCountryAnalyser() {
    totalAgePerCountry = new HashMap<>();
    countPerCountry = new HashMap<>();
  }

  /**
   * Processes a single line of data, adding the age to the total and incrementing the number of
   * people.
   *
   * @param personToProcess the person to process.
   */
  @Override
  public void processLine(Person personToProcess) {
    String country = personToProcess.getCountry();
    double age = personToProcess.getAge();

    totalAgePerCountry.put(country, totalAgePerCountry.getOrDefault(country, 0.0) + age);
    countPerCountry.put(country, countPerCountry.getOrDefault(country, 0) + 1);
  }

  /**
   * Outputs the average age of all people.
   */
  @Override
  public void outputResults() {
    System.out.println("\nAverage age per country:");
    totalAgePerCountry.forEach((country, totalAge) -> {
      System.out.printf("%s: %.1f\n", country, totalAge / countPerCountry.get(country));
    });
  }


  // Getters for testing
  public HashMap<String, Double> getTotalAgePerCountry() {
    return totalAgePerCountry;
  }

  public HashMap<String, Integer> getCountPerCountry() {
    return countPerCountry;
  }
}
