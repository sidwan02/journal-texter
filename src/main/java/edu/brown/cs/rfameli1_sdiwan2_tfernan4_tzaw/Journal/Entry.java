package edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.Journal;

import edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.JournalTexterDB;
import edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.wordCountVec.WordCountVec;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class to hold data for a given entry in the journal.
 * @param <T> a class that implements the JournalText interface
 */
public class Entry<T extends JournalText> {
  private final Integer id;
  private final LocalDate date;
  private final String stringRepresentation;
  private final List<T> questionsAndResponses;
  private final Integer sentiment = 0;
  private Set<String> tags = null;

  /**
   * Constructs an entry using a string representation of the entry.
   * @param date the date the entry was created
   * @param stringRepresentation a string representation representing the text of the entry
   */
  public Entry(Integer id, LocalDate date, String stringRepresentation) {
    // Responses will be represented as {response}
    // Questions will be represented as {@question}
    this.id = id;
    this.date = date;
    Pattern regexParse = Pattern.compile("{([^{}]*)}");
    Matcher m = regexParse.matcher(stringRepresentation);
    List<T> questionsResponses = new ArrayList<>();
    while (m.find()) {
      String questionOrResponseText = m.group();
      if (questionOrResponseText.charAt(0) == '@') {
        questionsResponses.add((T) new Question(questionOrResponseText));
      } else {
        questionsResponses.add((T) new Response(questionOrResponseText));
      }
    }
    this.questionsAndResponses = questionsResponses;
    this.stringRepresentation = stringRepresentation;
  }

  /**
   * Parameterized constructor for an entry.
   * @param date the date the entry was created
   * @param questionsAndResponses a List of Questions and Responses
   */
  public Entry(Integer id, LocalDate date, List<T> questionsAndResponses) {
    this.id = id;
    this.date = date;
    this.questionsAndResponses = questionsAndResponses;
    StringBuilder stringRep = new StringBuilder();
    // Iterate through every question/response
    for (T questionOrResponse : questionsAndResponses) {
      // Concatenate the questionOrResponse's stringRepresentation
      stringRep.append(questionOrResponse.stringRepresentation());
    }
    this.stringRepresentation = stringRep.toString();
  }

  /**
   * Retrieves the date of the entry.
   * @return a date in Java's Date format
   */
  public LocalDate getDate() {
    return this.date;
  }

  /**
   * Gets the string representation of the Entry.
   * @return a formatted String such as "\{question\}\{response\}"
   */
  public String getString() {
    return this.stringRepresentation;
  }

  /**
   * Gets the List of JournalText that represents the Questions and Responses in the Entry.
   * @return a List of JournalText
   */
  public List<T> getQuestionsAndResponses() {
    return this.questionsAndResponses;
  }


  private Map<String, Integer> getMostFrequentWords() {
    TreeMap<String, Integer> singleResponseMostFrequentWords;
    Map<String, Integer> allResponsesMostFrequentWords = new TreeMap<>();

    // Iterate through every JournalText, adding the most frequent words from each Response
    for (JournalText questionOrResponse : questionsAndResponses) {
      if (questionOrResponse.getType() == JournalTextType.RESPONSE) {
        WordCountVec vectorized = new WordCountVec();
        singleResponseMostFrequentWords =
            vectorized.getFrequenciesFromText(questionOrResponse.getText(), 1);

        for (String word : singleResponseMostFrequentWords.keySet()) {
          Integer currentCount = allResponsesMostFrequentWords.get(word);
          if (currentCount == null) {
            // If the word is not already in the Map, add it and its initial count
            allResponsesMostFrequentWords.put(word, singleResponseMostFrequentWords.get(word));
          } else {
            // If the word is already in the Map, update its count
            allResponsesMostFrequentWords.put(word,
                currentCount + singleResponseMostFrequentWords.get(word));
          }
        }
      }
    }
    return allResponsesMostFrequentWords;
    // TODO make a field so this can be stored and easily referenced without having to go
    // through this process each time
  }

  /**
   * Gets the tags from jtDatabase based on the most common words found in all responses.
   * @return a Map of tags and their frequencies in responses
   */
  public Set<String> getTags() throws SQLException {
    return this.tags; // <== instantiate this when retrieving from the database
  }

  public Integer getSentiment() {
    return this.sentiment;
  }

  /**
   * Analysis will be done on a combination of all responses
   * List<String> ==>
   */
}
