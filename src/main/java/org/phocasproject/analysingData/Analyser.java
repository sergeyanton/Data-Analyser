package org.phocasproject.analysingData;

import org.phocasproject.objects.Person;

/**
 * Interface for classes that analyse data.
 */
public interface Analyser {

  void processLine(Person personToProcess);

  void outputResults();

}
