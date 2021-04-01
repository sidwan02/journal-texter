package edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw;

import java.util.HashSet;
import java.util.Set;

public class Question {
  private final String text;
  private final Set<String> tags;
  public Question(String text, Set<String> tags) {
    this.text = text;
    this.tags = tags;
  }

  public Set<String> getTags() {
    return new HashSet<>(tags);
  }
  public String getText() {
    return text;
  }
}
