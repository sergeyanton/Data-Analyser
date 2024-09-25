package org.phocasproject;

import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.phocasproject.analysingData.AgeRangeInNZAnalyser;
import org.phocasproject.analysingData.Analyser;
import org.phocasproject.analysingData.AverageAgeAnalyser;
import org.phocasproject.analysingData.AverageAgePerCountryAnalyser;
import org.phocasproject.analysingData.OldestPersonAnalyser;
import org.phocasproject.analysingData.YoungestPersonPerCountryAnalyser;

public class App {

  private static final Logger logger = LogManager.getLogger(App.class);

  /**
   * Application entry point, accepts a filename as a command line argument and runs the
   * application
   *
   * @param args command line parameters
   */
  public static void main(String[] args) {
    logger.info("Starting application...");
    if (args.length < 1) {
      System.out.println("Please provide a filename as an argument.");
      System.exit(1);
    }
    // Set up the analysers
    OldestPersonAnalyser oldestPersonAnalyser = new OldestPersonAnalyser();
    AverageAgeAnalyser averageAgeAnalyser = new AverageAgeAnalyser();
    YoungestPersonPerCountryAnalyser youngestPersonPerCountryAnalyser = new YoungestPersonPerCountryAnalyser();
    AverageAgePerCountryAnalyser averageAgePerCountryAnalyser = new AverageAgePerCountryAnalyser();
    AgeRangeInNZAnalyser ageRangeInNZAnalyser = new AgeRangeInNZAnalyser();

    // initialise the list of analysers
    ArrayList<Analyser> analysers = new ArrayList<>(
        List.of(oldestPersonAnalyser, averageAgeAnalyser,
            youngestPersonPerCountryAnalyser, averageAgePerCountryAnalyser, ageRangeInNZAnalyser));
    CommandLineInterface.runApplication(args[0], analysers);
  }


}