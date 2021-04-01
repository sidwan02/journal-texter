package edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw;

public class QuestionResponse {
  private Question question;
  private String response;

  public String toString() {
    return "{" + question.getText() + "}{" + response + "}";
  }
  public Question getQuestion() { return this.question; }
  public String getResponse() { return this.response; }
}
