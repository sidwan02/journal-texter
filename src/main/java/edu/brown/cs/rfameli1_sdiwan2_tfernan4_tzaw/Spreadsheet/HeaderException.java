package edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.Spreadsheet;

/**
 * Exception class for when a spreadsheet's headers are incorrect.
 */
public class HeaderException extends Exception {
  /**
   * Exception constructor.
   * @param message - the message to be presented to the user on throw
   */
  public HeaderException(String message) {
    super(message);
  }
}
