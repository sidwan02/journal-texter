package edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Entry {
  private Date date;
  private String entryString;
  private List<QuestionResponse> questionResponses;

  /**
   * Constructor using QuestionResponses.
   * @param date
   * @param questionResponses
   */
  public Entry(Date date, List<QuestionResponse> questionResponses) {
    this.date = date;
    this.questionResponses = new ArrayList<>(questionResponses);
    String stringRepresentation = "";
    for (QuestionResponse qr : questionResponses) {
      stringRepresentation += qr.toString();
    }
    this.entryString = stringRepresentation;
  }

  // may create a separate constructor from the string representation of entries

}
