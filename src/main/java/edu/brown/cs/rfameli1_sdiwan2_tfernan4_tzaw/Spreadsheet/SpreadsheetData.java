package edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.Spreadsheet;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the data in a spreadsheet.
 */
public class SpreadsheetData {
  private final List<String> headers;
  private final List<List<String>> rows;

  /**
   * Creates a new instance of SpreadsheetData with the given headers and rows.
   * @param headers the headers that indicate column values
   * @param rows the actual rows of the spreadsheet
   */
  public SpreadsheetData(List<String> headers, List<List<String>> rows) {
    this.headers = headers;
    this.rows = rows;
  }

  /**
   * Gets the headers of the spreadsheet.
   * @return a List of Strings representing the headers
   */
  public List<String> getHeaders() {
    return new ArrayList<>(headers);
  }

  /**
   * Gets the rows of the spreadsheet.
   * @return a List of List of Strings representing rows in a spreadsheet
   */
  public List<List<String>> getRows() {
    //
    List<List<String>> outList = new ArrayList<>();
    for (List<String> row : rows) {
      outList.add(new ArrayList<>(row));
    }
    return outList;
  }
}
