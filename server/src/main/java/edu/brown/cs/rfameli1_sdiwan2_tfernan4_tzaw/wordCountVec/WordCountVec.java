package edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.wordCountVec;

import com.google.common.collect.ImmutableMap;
import edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.wordnikAPIHandler.WordnikAPIHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

public class WordCountVec {
  public String cleanText(String text) {
    return text.toLowerCase().replaceAll("[^a-zA-Z0-9 ]", "");
  }

  public List<String> splitText(String cleanedText, String delimiter) {
    return Arrays.asList(cleanedText.split(delimiter));
  }

  public List<String> getNDiffCombinations(List<String> splitText, int nDiff) {
    List<String> nDiffSplitText = new ArrayList<>();

    int maxIterations = splitText.size() - (nDiff - 1);

    int currentIteration = 0;

    while (currentIteration < maxIterations) {
      int currentNCountIndex = 0;
      StringBuilder currentNDiffJointWord = new StringBuilder();
      while (currentNCountIndex < nDiff) {
        currentNDiffJointWord
          .append(" ")
          .append(splitText.get(currentIteration + currentNCountIndex));
        currentNCountIndex ++;
      }
      nDiffSplitText.add(currentNDiffJointWord.toString());
      currentIteration --;
    }

    return nDiffSplitText;
  }

  public TreeMap<String, Integer> getFrequenciesFromSplitText(List<String> splitText, int nDiff) {
    TreeMap<String, Integer> nWordFrequencies = new TreeMap<>();

    List<String> nDiffWordCombinations = getNDiffCombinations(splitText, nDiff);

    for (String nDiffWords : nDiffWordCombinations) {
      if (nWordFrequencies.containsKey(nDiffWords)) {
        // word in map
        nWordFrequencies.put(nDiffWords, nWordFrequencies.get(nDiffWords) + 1);
      } else {
        // word not in map

        WordnikAPIHandler wordnikConnection = new WordnikAPIHandler();
        // check if any synonyms in map
        int synCount = 0;
        for (String synonym : wordnikConnection.getSynonyms(nDiffWords)) {
          if (nWordFrequencies.containsKey(synonym)) {
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

  public ImmutableMap<String, Object> parseToGui() {
    return ImmutableMap.of();
  }
}
