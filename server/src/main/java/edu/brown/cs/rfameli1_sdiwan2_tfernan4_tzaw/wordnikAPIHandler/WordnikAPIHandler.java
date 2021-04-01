package edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.wordnikAPIHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashSet;
import java.util.Set;

public class WordnikAPIHandler {
  public Set<String> getSynonyms(String word) {
    HttpHeaders header = new HttpHeaders();

    HttpEntity<String> entity = new HttpEntity<>("parameters", header);
    RestTemplate rt = new RestTemplate();

    ResponseEntity<WordSynonym> response = rt.exchange("https://api.wordnik.com/v4/word.json/earth/relatedWords?useCanonical=false&relationshipTypes=synonym&limitPerRelationshipType=10&api_key=70538348db6b42e43a5181e32070feebc0b303e293ed13a97",
      HttpMethod.GET, entity, WordSynonym.class);

    WordSynonym synonyms = response.getBody();

    assert synonyms != null;
    System.out.println("ha: " + synonyms.getSynonyms());

    return synonyms.getSynonyms();
  }
}
