package edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.APIHandlers.wordSyonyms;

import edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.APIHandlers.PropertiesReader;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class WordnikAPIHandler {
  public Set<String> getSynonyms(String word) {
    HttpHeaders header = new HttpHeaders();

    HttpEntity<String> entity = new HttpEntity<>("parameters", header);
    RestTemplate rt = new RestTemplate();

    // https://docs.spring.io/spring-android/docs/current/reference/html/rest-template.html
    ResponseEntity<String> response = rt.exchange("https://api.wordnik.com/v4/word.json/" + word + "/relatedWords?useCanonical=false&relationshipTypes=synonym&limitPerRelationshipType=10&api_key=" + PropertiesReader.getProperty("WORDNIK_KEY"),
      HttpMethod.GET, entity, String.class);

    String eventBody = response.getBody();
//    System.out.println(eventBody);

    String spliceToStart = eventBody.split("\\[", 3)[2];
    String spliceUntilEnd = spliceToStart.split("\\]", 2)[0];
//    System.out.println(spliceUntilEnd);

    Set<String> synonyms
      = Arrays.stream(spliceUntilEnd.split(",")).collect(Collectors.toSet());

    System.out.println(synonyms);

    return synonyms;
  }
}
