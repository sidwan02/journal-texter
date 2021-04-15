package edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.wordCountVec;

import com.google.common.collect.ImmutableMap;
import edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.APIHandlers.wordSyonyms.ProxiedSynonymFetcher;
import edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.APIHandlers.wordSyonyms.WordnikAPIHandler;

import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

public class WordCountVec {
  public String cleanText(String text) {
    List<String> stopwords = new ArrayList<>();
    try {
      stopwords = Files.readAllLines(Paths.get("data/stopwords-en.txt"));
    } catch (IOException e) {
      System.out.println("Stopwords path is wrong");
    }

    List<String> splitText = new LinkedList<>(Arrays.asList(text.toLowerCase().split(" ")));
    splitText.removeAll(stopwords);

    String cleanedText = String.join(" ", splitText);

    return cleanedText.replaceAll("[^a-zA-Z0-9 ]", "");
  }

  public List<String> splitText(String cleanedText, String delimiter) {
    return Arrays.asList(cleanedText.split(delimiter));
  }

  public List<String> getNDiffCombinations(List<String> splitText, int nDiff) {
    List<String> nDiffSplitText = new ArrayList<>();

    int maxIterations = splitText.size() - (nDiff - 1);

    int currentIteration = 0;
//    System.out.println("maxIterations: " + maxIterations);
    if (currentIteration >= maxIterations) {
      // suggests that there are fewer words than n-diff
      nDiffSplitText.add(String.join(" ", splitText));
    }
    while (currentIteration < maxIterations) {
//      System.out.println("currentIteration: " + currentIteration);
      int currentNCountIndex = 0;
      StringBuilder currentNDiffJointWord = new StringBuilder(splitText.get(currentIteration));
      try {
        while (currentNCountIndex < (nDiff - 1)) {
//          System.out.println("currentNCountIndex: " + currentNCountIndex);
          currentNDiffJointWord
            .append(" ")
            .append(splitText.get(currentIteration + currentNCountIndex + 1));
          currentNCountIndex ++;
        }
        nDiffSplitText.add(currentNDiffJointWord.toString());
        currentIteration ++;
      } catch (IndexOutOfBoundsException e) {
        System.out.println("index out of bounds");
      }
    }

    return nDiffSplitText;
  }

  public TreeMap<String, Integer> getFrequenciesFromSplitText(List<String> splitText, int nDiff) {
    TreeMap<String, Integer> nWordFrequencies = new TreeMap<>();

    List<String> nDiffWordCombinations = getNDiffCombinations(splitText, nDiff);

    for (String nDiffWords : nDiffWordCombinations) {
      if (nWordFrequencies.containsKey(nDiffWords)) {
        // word in map
        System.out.println("word in map");
        nWordFrequencies.put(nDiffWords, nWordFrequencies.get(nDiffWords) + 1);
      } else {
        // word not in map
        System.out.println("word NOT in map");

//        WordnikAPIHandler wordnikConnection = new WordnikAPIHandler();
         ProxiedSynonymFetcher prox = ProxiedSynonymFetcher.INSTANCE;
         Set<String> synonyms = prox.get(nDiffWords);
        // check if any synonyms in map
        int synCount = 0;
//        for (String synonym : wordnikConnection.getSynonyms(nDiffWords)) {
        for (String synonym : synonyms) {
//          System.out.println(synonym);
//          System.out.println("ambient");
//          System.out.println(nWordFrequencies.keySet());
//          System.out.println(synonym.equals("ambient"));
          if (nWordFrequencies.containsKey(synonym)) {
            System.out.println("synonym in map");
            nWordFrequencies.put(synonym, nWordFrequencies.get(synonym) + 1);
            synCount ++;
          }
        }
        // synonym not in map
        if (synCount == 0) {
          nWordFrequencies.put(nDiffWords, 1);
        }
      }
    }
    return nWordFrequencies;
  }

  public TreeMap<String, Integer> getFrequenciesFromText(String text, int nDiff) {
    List<String> splitText = splitText(cleanText(text), " ");
    return getFrequenciesFromSplitText(splitText, nDiff);
  }

  // https://stackoverflow.com/questions/2864840/treemap-sort-by-value
  public <K,V extends Comparable<? super V>>
  SortedSet<Map.Entry<K,V>> sortByValues(Map<K,V> map) {
    SortedSet<Map.Entry<K,V>> sortedEntries = new TreeSet<>(
      (e1, e2) -> {
        int res = e1.getValue().compareTo(e2.getValue());
        return res != 0 ? res : 1;
      }
    );
    sortedEntries.addAll(map.entrySet());
    return sortedEntries;
  }
}
