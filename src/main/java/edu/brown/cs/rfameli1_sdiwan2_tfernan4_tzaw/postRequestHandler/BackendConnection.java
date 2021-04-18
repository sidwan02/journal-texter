package edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.postRequestHandler;

import edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.APIHandlers.dialectTranslation.DialectType;
import edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.APIHandlers.dialectTranslation.TranslationsAPIHandler;
import edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.Journal.Entry;
import edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.Journal.JournalText;
import edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.Journal.Question;
import edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.JournalTexterDB.JournalTexterDB;
import edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.sentimentAnalysis.SentimentAnalysis;
import edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.wordCountVec.WordCountVec;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.SortedSet;

public final class BackendConnection {

  private BackendConnection() {
  }

  /**
   * Get the desired number of randomly generated questions.
   *
   * @param n the number of questions required.
   * @return a list of questions.
   */
  public static List<String> getRandomlyGeneratedQuestions(int n) throws SQLException {
    JournalTexterDB jtDB = JournalTexterDB.getInstance();

    Set<String> tags = jtDB.getAllTagsFromDB();

    List<String> randomlyChosenTags = new ArrayList<>();

    int size = tags.size();
    int counter = n;
    List<Integer> seenItems = new ArrayList<>();
    while (counter > 0) {
      int item = new Random().nextInt(size);
      if (!seenItems.contains(item)) {
        int i = 0;
        for (String tag : tags) {
          if (i == item) {
            seenItems.add(item);
            randomlyChosenTags.add(tag);
          }
          i++;
        }
        counter--;
      }
    }

    TranslationsAPIHandler translate = new TranslationsAPIHandler();

    // get questions associated with each tag
    List<String> questions = new ArrayList<>();
    for (String tag : randomlyChosenTags) {
      List<Question> questionsFromTag = jtDB.findQuestionsFromTag(tag);

      for (Question q : questionsFromTag) {
        // use the American dialect
        // In future updates this dialect can be dynamically changed
        // through the passing of another parameter to this method.
        questions.add(translate.convertToDialect(q.getText(), DialectType.AMERICAN));
        if (questions.size() >= 5) {
          break;
        }
      }
    }

    return questions;
  }

  /**
   * Get the tags from a user's responses.
   *
   * @param combinedResponses a combined string of all responses.
   * @return a set of all tags associated with the responses.
   */
  public static List<String> getTagsFromResponses(String combinedResponses) {
    WordCountVec vectorizor = new WordCountVec();

    Map<String, Integer> frequencies = vectorizor.getFrequenciesFromText(combinedResponses, 1);

    SortedSet<Map.Entry<String, Integer>> sortedFrequencies = vectorizor.sortByValues(frequencies);

    JournalTexterDB jtDB = JournalTexterDB.getInstance();
    Set<String> tags = new HashSet<>();
    try {
      tags = jtDB.getAllTagsFromDB();
    } catch (SQLException e) {
      e.printStackTrace();
    }

    List<String> foundTags = new ArrayList<>();
    for (Map.Entry<String, Integer> entry : sortedFrequencies) {
      if (tags.contains(entry.getKey())) {
        foundTags.add(entry.getKey());
      }
    }
    System.out.println(foundTags);
    return foundTags;
  }

  /**
   * Get questions of desired tags.
   *
   * @param foundTags the tags associated with a user's responses.
   * @return a list of questions.
   */
  public static List<String> getQuestionsFromTags(List<String> foundTags) throws SQLException {
    List<String> questions = new ArrayList<>();

    JournalTexterDB jtDB = JournalTexterDB.getInstance();

    for (String tag : foundTags) {
      List<Question> questionsFromTag = jtDB.findQuestionsFromTag(tag);
      for (Question q : questionsFromTag) {
        questions.add(q.getText());
        // only 5 questions at most can be sent to the frontend
        if (questions.size() >= 5) {
          break;
        }
      }
    }

    return questions;
  }

  /**
   * Get the sentiment from a user's responses.
   *
   * @param combinedResponses the responses of a user.
   * @return a number between 0 to 1, 0 being highly negative and 1 being highly positive.
   */
  public static double getSentimentFromResponses(String combinedResponses) {
    SentimentAnalysis senti = new SentimentAnalysis();

    return senti.getSentimentFromText(combinedResponses);
  }

  /**
   * Get the entries summary of a user.
   *
   * @param username unique username/ID of the person whose summary must be determined.
   * @return a list of questions.
   */
  public static JSONArray getEntriesSummaryFromUsername(String username) throws SQLException {
    JournalTexterDB jtDB = JournalTexterDB.getInstance();

    List<Entry<JournalText>> entries = jtDB.getUserEntriesByUsername(username);

    JSONArray jsonArray = new JSONArray();

    for (Entry<JournalText> entry : entries) {
      JSONObject jsonObject = new JSONObject();
      try {
        jsonObject.put("entryId", entry.getId());
        jsonObject.put("entryTitle", entry.getTitle());
        jsonObject.put("date", entry.getDate());
        jsonObject.put("tags", entry.getTags());
        jsonObject.put("sentiment", entry.getWeightedSentiment());
      } catch (Exception e) {
        e.printStackTrace();
      }
      jsonArray.put(jsonObject);
    }

    return jsonArray;
  }
}
