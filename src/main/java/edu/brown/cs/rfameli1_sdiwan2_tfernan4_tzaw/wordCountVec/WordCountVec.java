package edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.wordCountVec;

import edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.APIHandlers.wordSyonyms.ProxiedSynonymFetcher;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

public class WordCountVec {

  /**
   * Gets the synonyms of a desired word.
   *
   * @param text text to be cleaned.
   * @return cleaned text without stopwords, without consecutive spaces and all lowered.
   */
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

  /**
   * Gets the synonyms of a desired word.
   *
   * @param cleanedText cleaned text from cleanText method.
   * @param delimiter characters to split text by.
   * @return text split by the delimiter.
   */
  public List<String> splitText(String cleanedText, String delimiter) {
    return Arrays.asList(cleanedText.split(delimiter));
  }

  /**
   * Get the phrase tokens from nDiff words.
   *
   * @param splitText a list of delimited words.
   * @param nDiff number of words to be contained within each phrase token.
   * @return a list of tokens, each of word length nDiff.
   */
  public List<String> getNDiffCombinations(List<String> splitText, int nDiff) {
    List<String> nDiffSplitText = new ArrayList<>();

    int maxIterations = splitText.size() - (nDiff - 1);

    int currentIteration = 0;
    if (currentIteration >= maxIterations) {
      // suggests that there are fewer words than n-diff
      nDiffSplitText.add(String.join(" ", splitText));
    }
    while (currentIteration < maxIterations) {
      int currentNCountIndex = 0;
      StringBuilder currentNDiffJointWord = new StringBuilder(splitText.get(currentIteration));
      try {
        while (currentNCountIndex < (nDiff - 1)) {
          currentNDiffJointWord
            .append(" ").append(splitText.get(currentIteration + currentNCountIndex + 1));
          currentNCountIndex++;
        }
        nDiffSplitText.add(currentNDiffJointWord.toString());
        currentIteration++;
      } catch (IndexOutOfBoundsException e) {
        System.out.println("Ndiff: index out of bounds");
      }
    }

    return nDiffSplitText;
  }

  /**
   * Get a map of all tokens and the frequency of their occurrence.
   *
   * @param splitText a list of delimited words.
   * @param nDiff number of words to be contained within each phrase token.
   * @return a map of tokens and frequencies.
   */
  public TreeMap<String, Integer> getFrequenciesFromSplitText(List<String> splitText,
                                                              int nDiff) {
    TreeMap<String, Integer> nWordFrequencies = new TreeMap<>();

    List<String> nDiffWordCombinations = getNDiffCombinations(splitText, nDiff);

    for (String nDiffWords : nDiffWordCombinations) {
      if (nWordFrequencies.containsKey(nDiffWords)) {
        // word in map
        nWordFrequencies.put(nDiffWords, nWordFrequencies.get(nDiffWords) + 1);
      } else {
        // word not in map

        ProxiedSynonymFetcher prox = ProxiedSynonymFetcher.INSTANCE;
        Set<String> synonyms = prox.get(nDiffWords);
        // check if any synonyms in map
        int synCount = 0;
        for (String synonym : synonyms) {
          if (nWordFrequencies.containsKey(synonym)) {
//            System.out.println("synonym in map");
            nWordFrequencies.put(synonym, nWordFrequencies.get(synonym) + 1);
            synCount++;
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

  /**
   * Generate frequencies of tokens from a String.
   *
   * @param text cleaned text from cleanText method.
   * @param nDiff number of words to be contained within each phrase token.
   * @return the frequency map of tokens to occurrence.
   */
  public TreeMap<String, Integer> getFrequenciesFromText(String text, int nDiff) {
    List<String> splitText = splitText(cleanText(text), " ");
    return getFrequenciesFromSplitText(splitText, nDiff);
  }

  // https://stackoverflow.com/questions/2864840/treemap-sort-by-value
  /**
   * Sort the frequencies map into descending order of frequency.
   *
   * @param <K> .
   * @param <V> .
   * @param map the frequency map to be sorted.
   * @return a sorted Set of all tokens by descending order of frequency.
   */
  public <K, V extends Comparable<? super V>>
      SortedSet<Map.Entry<K, V>> sortByValues(Map<K, V> map) {
    SortedSet<Map.Entry<K, V>> sortedEntries = new TreeSet<>(
          (e1, e2) -> {
              int res = e1.getValue().compareTo(e2.getValue());
//              return res != 0 ? res : 1;
              return -1 * res;
          }
        );
    sortedEntries.addAll(map.entrySet());
    return sortedEntries;
  }
}
