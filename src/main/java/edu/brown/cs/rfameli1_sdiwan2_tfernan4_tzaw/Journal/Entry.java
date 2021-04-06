package edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.Journal;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Entry<T extends JournalText<TreeMap<String, Integer>>> {
  private Date date;
  private String entryString;
  private List<T> questionsAndResponses;
  private TreeMap<String, Integer> tags;

  public Entry(Date date, List<T> questionsAndResponses) {
    this.date = date;
    this.questionsAndResponses = questionsAndResponses;
    String stringRepresentation = "";
    TreeMap<String, Integer> singleResponseTags;
    Map<String, Integer> entryTagMap = new TreeMap<>();

    // Iterate through every question/response
    for (T questionOrResponse : questionsAndResponses) {
      // If it is a RESPONSE, add the tags to this entry's tags
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
      // Concatenate the questionOrResponse's stringRepresentation
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

}
