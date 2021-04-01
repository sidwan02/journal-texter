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
  public HashMap<Integer, String> dialectURLs = new HashMap<>(){{
    put(0, "");
    put(1, "https://pirate.monkeyness.com/api/translate?english=");
  }};

  public String convertToDialect(String sentence, DialectType type) {

    HttpHeaders header = new HttpHeaders();

    HttpEntity<String> entity = new HttpEntity<>("parameters", header);
    RestTemplate rt = new RestTemplate();

    if (!dialectURLs.get(type.ordinal()).equals("")) {
      // https://docs.spring.io/spring-android/docs/current/reference/html/rest-template.html
      ResponseEntity<String> response = rt.exchange(dialectURLs.get(type.ordinal()) + sentence,
        HttpMethod.GET, entity, String.class);

      String translatedSentence = response.getBody();
      System.out.println(translatedSentence);

      return translatedSentence;
    } else {
      return sentence;
    }
  }
}
