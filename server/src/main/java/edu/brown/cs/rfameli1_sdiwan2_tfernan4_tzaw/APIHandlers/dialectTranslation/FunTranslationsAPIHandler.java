package edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.APIHandlers.dialectTranslation;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;

public class FunTranslationsAPIHandler {
  public HashMap<Integer, String>

  public String convertToDialect(DialectType type) {

    HttpHeaders header = new HttpHeaders();

    HttpEntity<String> entity = new HttpEntity<>("parameters", header);
    RestTemplate rt = new RestTemplate();

    // https://docs.spring.io/spring-android/docs/current/reference/html/rest-template.html
    ResponseEntity<String> response = rt.exchange("https://api.wordnik.com/v4/word.json/earth/relatedWords?useCanonical=false&relationshipTypes=synonym&limitPerRelationshipType=10&api_key=70538348db6b42e43a5181e32070feebc0b303e293ed13a97",
      HttpMethod.GET, entity, String.class);

    String translatedSentence = response.getBody();
    System.out.println(translatedSentence);

    return translatedSentence;
  }
}
