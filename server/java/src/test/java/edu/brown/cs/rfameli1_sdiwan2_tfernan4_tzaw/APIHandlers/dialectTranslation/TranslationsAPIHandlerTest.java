package edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.APIHandlers.dialectTranslation;

import edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.APIHandlers.wordSyonyms.WordnikAPIHandler;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class TranslationsAPIHandlerTest {
  @Test
  public void convertToDialect_OriginalText() {
    TranslationsAPIHandler trans = new TranslationsAPIHandler();
    String text = "Hello there everybody I'm really happy because haha yup I don't know why please help";
    String translated = trans.convertToDialect(text, DialectType.AMERICAN);

    assertEquals(text, translated);
  }

  @Test
  public void convertToDialect_PirateText() {
    TranslationsAPIHandler trans = new TranslationsAPIHandler();
    String text = "Hello there everybody I'm really happy because haha yup I don't know why please help";
    String translated = trans.convertToDialect(text, DialectType.PIRATE);

    String expectedTranslation = "Ahoy thar everybody I be mighty happy 'cause har har har aye I dunno why please help";

    assertEquals(expectedTranslation, translated);
  }
}
