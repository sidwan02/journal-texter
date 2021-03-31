package java.edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw;

import java.util.ArrayList;
import java.util.List;

public class Question {
  private final String text;
  private final List<String> tags;
  public Question(String text, List<String> tags) {
    this.text = text;
    this.tags = tags;
  }

  public List<String> getTags() {
    return new ArrayList<>(tags);
  }

  public String getText() {
    return text;
  }
}
