package edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.APIHandlers.wordSynonyms;

import edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.APIHandlers.wordSyonyms.WordnikAPIHandler;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class WordnikAPIHandlerTest {
  @Test
  public void getSynonyms_WordHasSynonyms() {
    WordnikAPIHandler wordnik = new WordnikAPIHandler();
    Set<String> synonyms = wordnik.getSynonyms("piano");

    Set<String> expectedSynonyms = new HashSet<>() {{
      add("adagietto");
      add("a cappella");
      add("accrescendo");
      add("adagio");
      add("affettuoso");
      add("Klavier");
      add("abbandono");
      add("agilmente");
      add("Steinway");
      add("a la sourdine");
    }};

//    assertEquals(synonyms, expectedSynonyms);
  }

  @Test
  public void getSynonyms_WordDoesNotHaveSynonyms() {
    WordnikAPIHandler wordnik = new WordnikAPIHandler();
    Set<String> synonyms = wordnik.getSynonyms("somethingthatdoesntexist");

    assertEquals(synonyms, new HashSet<>());
  }
}
