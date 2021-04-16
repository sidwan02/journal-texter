package edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.APIHandlers.dialectTranslation;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

/**
 * Class to determine what dialect to translate incoming text to through an API.
 */
public class TranslationsAPIHandler {
  /**
   * API URLs to translate text.
   */
  private final HashMap<Integer, String> dialectURLs = new HashMap<>() {
    {
      put(0, "");
      put(1, "https://pirate.monkeyness.com/api/translate?english=");
    }
  };

  /**
   * Converts text to the desired dialect.
   *
   * @param sentence a sentence to translate.
   * @param type     an ENUM representing the dialect to translate to.
   * @return the converted sentence to the desired dialect.
   */
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
      // no translation
      return sentence;
    }
  }
}
