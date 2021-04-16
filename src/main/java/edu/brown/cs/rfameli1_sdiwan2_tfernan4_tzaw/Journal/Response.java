package edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.Journal;

/**
 * Represents a Response in a JournalTexter Entry.
 */
public class Response implements JournalText {
  private final String text;

  /**
   * Parameterized constructor.
   * @param responseText the text inputted by the user as a response
   */
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
}
