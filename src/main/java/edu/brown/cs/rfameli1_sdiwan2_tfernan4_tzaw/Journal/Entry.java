package edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.Journal;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Entry<T extends JournalText<Map<Integer, String>>> {
  private Date date;
  private String entryString;
  private List<T> questionsAndResponses;
  private TreeMap<String, Integer> tags;

  public Entry(Date date, List<T> questionsAndResponses) {
    this.date = date;
    this.questionsAndResponses = questionsAndResponses;
    String stringRepresentation = "";
    Map<String, Integer> singleResponseTags;
    Map<String, Integer> entryTagMap = new TreeMap<>();
    for (T questionOrResponse : questionsAndResponses) {
      if (questionOrResponse.getType() == JournalTextType.RESPONSE) {
        singleResponseTags = questionOrResponse.getTags();
        for (String tag : singleResponseTags.keySet()) {
          Integer currentCount = entryTagMap.get(tag);
          if (currentCount == null) {
            entryTagMap.put(tag, singleResponseTags.get(tag));
          } else {
            entryTagMap.put(tag, currentCount + singleResponseTags.get(tag));
          }
        }
      }
      stringRepresentation += questionOrResponse.stringRepresentation();
    }
    this.entryString = stringRepresentation;
  }

  public Date getDate() {
    return this.date;
  }

  public String getString() {
    return this.entryString;
  }

  public List<T> getQuestionsAndResponses() {
    return this.questionsAndResponses;
  }

  public Map<String, Integer> getTagMap() {
    return this.tags;
  }
//  /**
//   * Constructor using QuestionResponses.
//   * @param date
//   * @param questionResponses
//   */
//  public Entry(Date date, List<QuestionResponse> questionResponses) {
//    this.date = date;
//    this.questionResponses = new ArrayList<>(questionResponses);
//    String stringRepresentation = "";
//    for (QuestionResponse qr : questionResponses) {
//      stringRepresentation += qr.toString();
//    }
//    this.entryString = stringRepresentation;
//  }

  // may create a separate constructor from the string representation of entries

}
