package edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.postRequestHandler;

import edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.Journal.Question;
import edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.JournalTexterDB;
import edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.sentimentAnalysis.SentimentAnalysis;
import edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.wordCountVec.WordCountVec;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

public class BackendConnection {
  public BackendConnection() {
  }

  public static List<String> getRandomlyGeneratedQuestions(int n) {
    JournalTexterDB jtDB = new JournalTexterDB();
    Set<String> tags = jtDB.getAllTagsFromDB();

    List<String> randomlyChosenTags = new ArrayList<>();
    for (String tag : tags) {
      randomlyChosenTags.add(tag);
      if (randomlyChosenTags.size() == n) {
        break;
      }
    }

    List<String> questions = new ArrayList<>();
    for (String tag : randomlyChosenTags) {
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

  public static Set<String> getTagsFromResponses(String combinedResponses) {
    WordCountVec vectorizor = new WordCountVec();

    Map<String, Integer> frequencies
      = vectorizor.getFrequenciesFromText(combinedResponses, 1);

    SortedSet<Map.Entry<String, Integer>> sortedFrequencies
      = vectorizor.sortByValues(frequencies);

    JournalTexterDB jtDB = new JournalTexterDB();
    Set<String> tags = jtDB.getAllTagsFromDB();

    List<String> foundTags = new ArrayList<>();
    for (Map.Entry<String, Integer> entry : sortedFrequencies) {
      if (tags.contains(entry.getKey())) {
        foundTags.add(entry.getKey());
      }
    }

    return foundTags;
  }

  public static List<String> getQuestionsFromTags(Set<String> foundTags) {
    List<String> questions = new ArrayList<>();

    JournalTexterDB jtDB = new JournalTexterDB();

    for (String tag : foundTags) {
      List<Question> questionsFromTag = jtDB.findQuestionsFromTag(tag);
      for (Question q : questionsFromTag) {
        questions.add(q.getText());
        if (questions.size() >= 5) {
          break;
        }
      }
    }
  }

  public static double getSentimentFromResponses(String combinedResponses) {
    SentimentAnalysis senti = new SentimentAnalysis();
    Double sentiment = senti.getSentimentFromText(combinedResponses);
  }
}
