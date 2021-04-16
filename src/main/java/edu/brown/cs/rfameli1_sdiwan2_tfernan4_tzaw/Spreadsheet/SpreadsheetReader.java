package edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.Spreadsheet;

import java.io.BufferedReader;
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
    BufferedReader br;
    try {
      br = new BufferedReader(new FileReader(filename));
    } catch (IOException e) {
      throw new IOException("File " + filename + " not found or could not be read");
    }

    String line;
    line = br.readLine();

    // return null if given an empty file
    if (line == null) {
      return null;
    }

    // check that fileHeaders match the input headers
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
    while ((line = br.readLine()) != null) {
      // Convert each line into a List<String>, with Strings representing comma-separated values
      List<String> cleanedLine = Arrays.asList(line.split(separator, -1));
      if (cleanedLine.size() != fileHeaders.size()) {
        throw new HeaderException("Found row longer than number of headers in file " + filename);
      }
      allLines.add(cleanedLine);
    }
    return new SpreadsheetData(fileHeaders, allLines);
  }


}
