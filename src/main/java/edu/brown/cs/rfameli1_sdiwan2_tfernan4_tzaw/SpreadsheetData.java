package edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw;

import java.util.ArrayList;
import java.util.List;

public class SpreadsheetData {
  private List<String> headers;
  private List<List<String>> data;

  public SpreadsheetData(List<String> headers, List<List<String>> data) {
    this.headers = headers;
    this.data = data;
  }

  public List<String> headers() {
    return new ArrayList<>(headers);
  }

  public List<List<String>> data() {
    // TODO make defensive copies of inner lists too
    return new ArrayList<>(data);
  }
}
