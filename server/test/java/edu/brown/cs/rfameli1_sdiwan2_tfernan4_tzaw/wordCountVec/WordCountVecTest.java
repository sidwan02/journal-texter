package edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.wordCountVec;

import org.junit.Test;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class WordCountVecTest {
  @Test
  public void testCleanText_TextAlreadyClean() {
    String text = "this text is already clean and is already split up wow pretty cool";

    WordCountVec vectorizer = new WordCountVec();
    assertEquals(vectorizer.cleanText(text), text);
  }
}
