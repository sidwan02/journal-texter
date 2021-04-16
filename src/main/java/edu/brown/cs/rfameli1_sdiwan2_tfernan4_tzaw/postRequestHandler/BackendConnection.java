package edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.postRequestHandler;

import edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.APIHandlers.dialectTranslation.DialectType;
import edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.APIHandlers.dialectTranslation.TranslationsAPIHandler;
import edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.Journal.Entry;
import edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.Journal.JournalText;
import edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.Journal.Question;
import edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.JournalTexterDB;
import edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.sentimentAnalysis.SentimentAnalysis;
import edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.wordCountVec.WordCountVec;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.SortedSet;

public class BackendConnection {

  public BackendConnection() {
  }

  public static List<String> getRandomlyGeneratedQuestions(int n) throws SQLException {
    JournalTexterDB jtDB = JournalTexterDB.getInstance();

    Set<String> tags = jtDB.getAllTagsFromDB();

    List<String> randomlyChosenTags = new ArrayList<>();

    int size = tags.size();
    int counter = n;
    while (counter > 0) {
      int item = new Random().nextInt(size);
      int i = 0;
      for(String tag : tags)
      {
        if (i == item)
//          return tag;
          randomlyChosenTags.add(tag);
        i++;
      }

//      if (randomlyChosenTags.size() == n) {
//        break;
//      }
      counter --;
    }

    TranslationsAPIHandler translate = new TranslationsAPIHandler();

    List<String> questions = new ArrayList<>();
    for (String tag : randomlyChosenTags) {
      List<Question> questionsFromTag = jtDB.findQuestionsFromTag(tag);

      for (Question q : questionsFromTag) {
        questions.add(translate.convertToDialect(q.getText(), DialectType.AMERICAN));
        if (questions.size() >= 5) {
          break;
        }
      }
    }

    return questions;
  }

  public static Set<String> getTagsFromResponses(String combinedResponses) throws SQLException {
    WordCountVec vectorizor = new WordCountVec();

    Map<String, Integer> frequencies
      = vectorizor.getFrequenciesFromText(combinedResponses, 1);

    SortedSet<Map.Entry<String, Integer>> sortedFrequencies
      = vectorizor.sortByValues(frequencies);

    JournalTexterDB jtDB = JournalTexterDB.getInstance();
    Set<String> tags = new HashSet<>();
    try {
      tags = jtDB.getAllTagsFromDB();
    } catch (SQLException e) {
      e.printStackTrace();
    }

    Set<String> foundTags = new HashSet<>();
    for (Map.Entry<String, Integer> entry : sortedFrequencies) {
      if (tags.contains(entry.getKey())) {
        foundTags.add(entry.getKey());
      }
    }

    return foundTags;
  }

  public static List<String> getQuestionsFromTags(Set<String> foundTags) throws SQLException {
    List<String> questions = new ArrayList<>();

    //JournalTexterDB jtDB = new JournalTexterDB();
    JournalTexterDB jtDB = JournalTexterDB.getInstance();

    for (String tag : foundTags) {
      List<Question> questionsFromTag = jtDB.findQuestionsFromTag(tag);
      for (Question q : questionsFromTag) {
        questions.add(q.getText());
        if (questions.size() >= 5) {
          break;
        }
      }
    }

    return questions;
  }

  public static double getSentimentFromResponses(String combinedResponses) {
    SentimentAnalysis senti = new SentimentAnalysis();
    Double sentiment = senti.getSentimentFromText(combinedResponses);

    return sentiment;
  }

  public static JSONArray getEntriesSummaryFromUsername(String username) throws SQLException {
    //JournalTexterDB jtDB = new JournalTexterDB();
    JournalTexterDB jtDB = JournalTexterDB.getInstance();

    List<Entry<JournalText>> entries = jtDB.getUserEntriesByUsername(username);

    JSONArray jsonArray = new JSONArray();

    for (Entry<JournalText> entry : entries) {
      JSONObject jsonObject = new JSONObject();
      try {
      jsonObject.put("entryId", entry.getId());
      jsonObject.put("date", entry.getDate());
      jsonObject.put("tags", entry.getTags());
      jsonObject.put("sentiment", entry.getSentiment());
      } catch (Exception e) {
        e.printStackTrace();
      }
      jsonArray.put(jsonObject);
    }

    return jsonArray;
  }
}
