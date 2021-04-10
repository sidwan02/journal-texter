package edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.Journal;

/**
 * An interface to represent the different types of text that may appear in a JournalTexter journal
 * entry (e.g. Questions and Responses).
 */
public interface JournalText {
  /**
   * Gets the type of the JournalText.
   * @return a JournalTextType representing the type of the JournalText
   */
  JournalTextType getType();

  /**
   * Gets the plain text of the JournalText.
   * @return a String of the text
   */
  String getText();

  /**
   * Creates a string representation of the JournalText based on its type.
   * @return a String representing the JournalText's type and its text
   */
  String stringRepresentation();
}
