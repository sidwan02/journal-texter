package edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Utility class for reading and parsing spreadsheets.
 */
public final class SpreadsheetReader {

  /**
   * Private constructor (utility class).
   */
  private SpreadsheetReader() { }

  /**
   * Parses a spreadsheet into a SpreadsheetData.
   * @param filename the name of the spreadsheet file
   * @param separator the character for separating fields
   * @param headers the expected headers for the file
   * @return a SpreadsheetData representing the spreadsheet, or null if the file is empty
   * @throws IOException if an issue occurs reading in the file
   * @throws HeaderException if the headers do not match those that were passed in as an input
   */
  public static SpreadsheetData parseSpreadsheet(String filename, String separator,
                                                    List<String> headers)
      throws HeaderException, IOException {
    BufferedReader br = new BufferedReader(new FileReader(filename));

    String line;
    line = br.readLine();

    // return null if given an empty file
    if (line == null) {
      return null;
    }

    List<String> fileHeaders = Arrays.asList(line.split(separator));

    if (!fileHeaders.equals(headers)) {
      throw new HeaderException("Headers do not match the provided headers given in "
          + "parseSpreadsheet");
    }

    // return null if there are no headers
    if (fileHeaders.size() == 0) {
      return null;
    }

    List<List<String>> allLines = new ArrayList<>();
    List<String> cleanedLine = new ArrayList<>();
    while ((line = br.readLine()) != null) {
      // Convert each line into a List<String>, with Strings representing comma-separated values
      cleanedLine = Arrays.asList(line.split(separator, -1));
      allLines.add(cleanedLine);
    }

    return new SpreadsheetData(fileHeaders, allLines);
  }

  /**
   * Exception class for when a spreadsheet's headers are incorrect.
   */
  public static class HeaderException extends Exception {
    /**
     * Exception constructor.
     * @param message - the message to be presented to the user on throw
     */
    public HeaderException(String message) {
      super(message);
    }
  }
}
