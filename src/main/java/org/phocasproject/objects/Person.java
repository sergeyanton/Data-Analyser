package org.phocasproject.objects;

/**
 * Person class to store information about a person parsed from a file.
 */
public class Person {

  private String name;
  private String country;
  private double age;

  /**
   * Constructor that initialises the person with a name, country and age.
   *
   * @param name    the name of the person.
   * @param country the country of the person.
   * @param age     the age of the person.
   */
  public Person(String name, String country, int age) {
    this.name = name;
    this.country = country;
    this.age = age;
  }

  public String getName() {
    return name;
  }

  public String getCountry() {
    return country;
  }

  public double getAge() {
    return age;
  }

  @Override
  public String toString() {
    return String.format("%s, %s, %d", name, country, (int) age);
  }
}
