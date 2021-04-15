package edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.Spreadsheet;

/**
 * Exception for when a file is invalid.
 */
public class InvalidFileException extends Exception {
  /**
   * Exception constructor.
   * @param message the message to be displayed to the user on throwing
   */
  public InvalidFileException(String message) {
    super(message);
  }
}
