package edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.wordnikAPIHandler;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashSet;
import java.util.Set;

public class WordSynonym {
  private String word;
  private Set<String> synonyms;

  public WordSynonym(String word, Set<String> synonyms) {
    this.word = word;
    this.synonyms = new HashSet<>(synonyms);
  }

  public String getWord() {
    return word;
  }

  public Set<String> getSynonyms() {
    return new HashSet<>(synonyms);
  }

  @Override
  public String toString() {
    return "WordSynonym{" +
      "word='" + word + '\'' +
      ", synonyms=" + synonyms +
      '}';
  }
}
