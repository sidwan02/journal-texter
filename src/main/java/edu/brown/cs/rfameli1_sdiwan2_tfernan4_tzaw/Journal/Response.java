package edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.Journal;

import java.util.TreeMap;

public class Response implements JournalText<TreeMap<String, Integer>> {
  private String text;
  private TreeMap<String, Integer> tags = null;

  public Response(String responseText) {
    this.text = responseText;
  }

  @Override
  public JournalTextType getType() {
    return JournalTextType.RESPONSE;
  }

  @Override
  public String getText() {
    return text;
  }

  @Override
  public String stringRepresentation() {
    return "{" + text + "}";
  }

  @Override
  public TreeMap<String, Integer> getTags() {
    // TODO add wordVecCounter stuff in here
    return this.tags;
  }
}
