package org.phocasproject;

import java.io.IOException;
import java.util.ArrayList;
import org.phocasproject.analysingData.Analyser;
import org.phocasproject.util.InputFileHandler;

/**
 * Class to handle the command line interface of the application.
 */
public class CommandLineInterface {

  /**
   * Application entry point, accepts a filename and runs the application.
   *
   * @param filename command line parameters
   */
  public static void runApplication(String filename, ArrayList<Analyser> analysers) {
    try {
      ArrayList<Analyser> finishedAnalysers = InputFileHandler.readPersonsFromFile(filename,
          analysers);
      displayAnalytics(finishedAnalysers);
    } catch (IOException e) {
      System.err.println("Error reading the file: " + e.getMessage());
    }
  }

  /**
   * Displays the results of the analysers.
   *
   * @param finishedAnalysers the list of finished analysers.
   */
  private static void displayAnalytics(ArrayList<Analyser> finishedAnalysers) {
    finishedAnalysers.forEach(Analyser::outputResults);
  }


}
