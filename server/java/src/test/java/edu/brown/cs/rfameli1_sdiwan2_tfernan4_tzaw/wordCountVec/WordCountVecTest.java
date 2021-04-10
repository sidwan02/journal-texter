package edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.wordCountVec;

import org.checkerframework.checker.units.qual.A;
import org.junit.Test;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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

  @Test
  public void testCleanText_TextContainsNonAlphanumeric() {
    String text = "this text is definitely already clean and it's got some pun!ua|ons t00";

    String textCleaned = "this text is definitely already clean and its got some punuaons t00";

    WordCountVec vectorizer = new WordCountVec();
    assertEquals(vectorizer.cleanText(text), textCleaned);
  }

  @Test
  public void testCleanText_TextContainsCapital() {
    String text = "this text is DEFINITELY NOT lowerCaSe";

    String textCleaned = "this text is definitely not lowercase";

    WordCountVec vectorizer = new WordCountVec();
    assertEquals(vectorizer.cleanText(text), textCleaned);
  }

  @Test
  public void testSplitText_TextContainsNoSpaces() {
    String text = "hahahehehehohohohowdoyoudoImfinethankyouifyouarereadingthismessaeknowthatyouareappreciatedhaveagreatdaythankyou";

    WordCountVec vectorizer = new WordCountVec();
    assertEquals(vectorizer.splitText(text, " "), new ArrayList<>(Collections.singleton(text)));
  }

  @Test
  public void testSplitText_TextContainsSomeSpaces() {
    String text = "i have many spaces";

    List<String> splitText = new ArrayList<>();

    splitText.add("i");
    splitText.add("have");
    splitText.add("many");
    splitText.add("spaces");

    WordCountVec vectorizer = new WordCountVec();
    assertEquals(vectorizer.splitText(text, " "), splitText);
  }

  @Test
  public void testSplitText_TextContainsOnlySpaces() {
    String text = "           ";

    WordCountVec vectorizer = new WordCountVec();
    assertEquals(vectorizer.splitText(text, " "), new ArrayList<>());
  }

  @Test
  public void testGetNDiffCombinations_1Diff_TextContainsSingleWord() {
    WordCountVec vectorizer = new WordCountVec();

    List<String> text = vectorizer.splitText("hello", " ");

    List<String> expectedOutput = new ArrayList<>();
    expectedOutput.add("hello");

    assertEquals(vectorizer.getNDiffCombinations(text, 1), expectedOutput);
  }

  @Test
  public void testGetNDiffCombinations_1Diff_TextContainsMultipleWords() {
    WordCountVec vectorizer = new WordCountVec();

    List<String> text = vectorizer.splitText("lorem ipsum dolor sit amet consectetur adipiscing elit", " ");

    List<String> expectedOutput = new ArrayList<>();
    expectedOutput.add("lorem");
    expectedOutput.add("ipsum");
    expectedOutput.add("dolor");
    expectedOutput.add("sit");
    expectedOutput.add("amet");
    expectedOutput.add("consectetur");
    expectedOutput.add("adipiscing");
    expectedOutput.add("elit");

    assertEquals(vectorizer.getNDiffCombinations(text, 1), expectedOutput);
  }

  @Test
  public void testGetNDiffCombinations_2Diff_TextContainsSingleWord() {
    WordCountVec vectorizer = new WordCountVec();

    List<String> text = vectorizer.splitText("hello", " ");

    List<String> expectedOutput = new ArrayList<>();
    expectedOutput.add("hello");

    assertEquals(vectorizer.getNDiffCombinations(text, 2), expectedOutput);
  }

  @Test
  public void testGetNDiffCombinations_2Diff_TextContainsMultipleWords() {
    WordCountVec vectorizer = new WordCountVec();

    List<String> text = vectorizer.splitText("lorem ipsum dolor sit amet consectetur adipiscing elit", " ");

    List<String> expectedOutput = new ArrayList<>();
    expectedOutput.add("lorem ipsum");
    expectedOutput.add("ipsum dolor");
    expectedOutput.add("dolor sit");
    expectedOutput.add("sit amet");
    expectedOutput.add("amet consectetur");
    expectedOutput.add("consectetur adipiscing");
    expectedOutput.add("adipiscing elit");

    assertEquals(vectorizer.getNDiffCombinations(text, 2), expectedOutput);
  }

  @Test
  public void testGetNDiffCombinations_5Diff() {
    WordCountVec vectorizer = new WordCountVec();

    List<String> text = vectorizer.splitText("lorem ipsum dolor sit amet consectetur adipiscing elit", " ");

    List<String> expectedOutput = new ArrayList<>();
    expectedOutput.add("lorem ipsum dolor sit amet");
    expectedOutput.add("ipsum dolor sit amet consectetur");
    expectedOutput.add("dolor sit amet consectetur adipiscing");
    expectedOutput.add("sit amet consectetur adipiscing elit");

    assertEquals(vectorizer.getNDiffCombinations(text, 5), expectedOutput);
  }
}
