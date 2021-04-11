package edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.sentimentAnalysis;

import org.checkerframework.checker.units.qual.A;
import org.junit.Test;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static org.junit.Assert.*;

public class SentimentAnalysisTest {
  @Test
  public void getSentimentFromText_PositiveReview() {
    SentimentAnalysis senti = new SentimentAnalysis();
    double sentiment = senti.getSentimentFromText("An excellent look at the inner lives of these heroes, one that simply wasn't on offer in the films.");
    assertTrue(sentiment > 0.5);
  }

  @Test
  public void getSentimentFromText_NegativeReview() {
    SentimentAnalysis senti = new SentimentAnalysis();
    double sentiment = senti.getSentimentFromText("\n" +
      "Can't say this one is much of an improvement other than Cyborg's character development. Overall I just don't see much artistic merit in Snyder's cut, but fan service rarely make for a good movie.");
    assertTrue(sentiment < 0.5);
  }
}
