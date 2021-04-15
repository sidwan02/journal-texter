package edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.APIHandlers.wordSyonyms;

import edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.APIHandlers.PropertiesReader;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Class that connects to the Wordnik thesaurus API.
 */
public class WordnikAPIHandler {
  /**
   * Gets the synonyms of a desired word.
   * @param word a word whose synonyms must be found.
   * @return a Set of String of all synonyms associated with the passed word.
   */
  public Set<String> getSynonyms(String word) {
    HttpHeaders header = new HttpHeaders();

    HttpEntity<String> entity = new HttpEntity<>("parameters", header);
    RestTemplate rt = new RestTemplate();

    // https://docs.spring.io/spring-android/docs/current/reference/html/rest-template.html
    try {
      ResponseEntity<String> response
        = rt.exchange(
          "https://api.wordnik.com/v4/word.json/"
          + word
          + "/relatedWords?useCanonical=false&relationshipTypes=synonym&limitPerRelationshipType=10&api_key="
          + PropertiesReader.getProperty("WORDNIK_KEY"),
        HttpMethod.GET, entity, String.class);

      String eventBody = response.getBody();

      // extract all synonyms from the stringified object from the API
      assert eventBody != null;
      String spliceToStart = eventBody.split("\\[", 3)[2];
      String spliceUntilEnd = spliceToStart.split("\\]", 2)[0];

      return Arrays.stream(spliceUntilEnd.replaceAll("\"", "")
        .split(",")).collect(Collectors.toSet());
    } catch (HttpClientErrorException e) {
      // word does not have synonyms within the Wordnik API
      return new HashSet<>();
    }
  }
}
