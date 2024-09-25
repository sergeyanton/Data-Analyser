package org.phocasproject.analysingData;

import org.phocasproject.objects.Person;

/**
 * Analyser that calculates the average age of all people.
 */
public class AverageAgeAnalyser implements Analyser {

  private double totalAge;
  private int numberOfPeople;

  /**
   * Constructor that initialises the total age and number of people.
   */
  public AverageAgeAnalyser() {
    totalAge = 0;
    numberOfPeople = 0;
  }

  /**
   * Processes a single line of data, adding the age to the total and incrementing the number of
   * people.
   *
   * @param personToProcess the person to process.
   */
  @Override
  public void processLine(Person personToProcess) {
    totalAge += personToProcess.getAge();
    numberOfPeople++;
  }

  /**
   * Outputs the average age of all people.
   */
  @Override
  public void outputResults() {
    System.out.printf("\n\nThe average age of all people is: %.1f \n", totalAge / numberOfPeople);
  }


  // Getters for testing
  public double getTotalAge() {
    return totalAge;
  }

  public int getNumberOfPeople() {
    return numberOfPeople;
  }
}
