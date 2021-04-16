package edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.REPL;

import java.util.List;

/**
 * Represents a command in the REPL.
 */
public interface REPLCommand {
  /**
   * Gets the word that will be used to call the command.
   * @return a String representing the words that can be used to call the command
   */
  String getCallWord();

  /**
   * Gets a List of possible ArgFormats.
   * @return a List of List of ArgTypes
   */
  List<List<ArgTypes>> getArgFormats();

  /**
   * Runs the command with an ArgHolder with all provided arguments.
   * @param arguments an ArgHolder from which argument values can be retrieved
   */
  void run(ArgHolder arguments);
}
