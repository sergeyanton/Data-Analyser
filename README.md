# Person Data Analyzer

A Java command-line program that processes JSON data to extract key demographic statistics.

## Features
- **Oldest person** - Identifies the person with the highest age
- **Average age** - Calculates mean age across all valid entries
- **Youngest person per country** - Finds the youngest individual for each country
- **Average age per country** - Computes mean age per country
- **NZ age distribution** - Counts people in 10-year age brackets (0-9, 10-19, etc.)

## Requirements
- Java JDK 8 or later
- Input JSON file containing person data

## Assumptions:
- If there is a malformed object in the input json file it will be ignored.
- Any errors will be logged to output. 
- The user will not have the option to select the output option, they will see all required information after executing the command. 
