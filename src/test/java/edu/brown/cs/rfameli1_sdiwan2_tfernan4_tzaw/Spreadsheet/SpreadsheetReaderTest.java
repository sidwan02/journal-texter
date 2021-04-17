package edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.Spreadsheet;

import org.junit.Test;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class SpreadsheetReaderTest {

  @Test
  public void testParseSpreadsheet() throws IOException, HeaderException {
    SpreadsheetData sd = SpreadsheetReader.parseSpreadsheet(
        "data/testdata/test-spreadsheet1.csv", ",", Arrays.asList("First", "", "Third"));
    assertEquals(Arrays.asList("First", "", "Third"), sd.getHeaders());
    List<List<String>> rows = sd.getRows();
    assertEquals(Arrays.asList("a", "b", "c"), rows.get(0));
    assertEquals(Arrays.asList("d", "", "f"), rows.get(1));

    sd = SpreadsheetReader.parseSpreadsheet(
        "data/testdata/empty-spreadsheet.tsv", "\t", Collections.emptyList());
    assertNull(sd);

    sd = SpreadsheetReader.parseSpreadsheet(
        "data/testdata/test-spreadsheet2.tsv", "\t", Arrays.asList("question", "tags"));
    assertEquals(Arrays.asList("question", "tags"), sd.getHeaders());
    assertEquals(Arrays.asList("How are you?", "a,b,c"), sd.getRows().get(0));
  }
}
