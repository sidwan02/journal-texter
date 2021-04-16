package edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.JournalTexterREPL;

import edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.JournalTexterDB;
import edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.REPL.REPL;

import java.io.IOException;

/**
 * The REPL for JournalTexter.
 */
public class JournalTexterREPL {
  private final REPL repl;
  private final JournalTexterDB jtDatabase;

  /**
   * Parameterized constructor.
   * @param repl the REPL to be used in the program.
   * @param jtDatabase the JournalTexterDB to be used.
   */
  public JournalTexterREPL(REPL repl, JournalTexterDB jtDatabase) {
    this.repl = repl;
    this.jtDatabase = jtDatabase;
  }

  /**
   * Register the commands for the REPL.
   */
  public void registerCommands() {
    // Commands that take in jtDatabase are able to access/modify the database
    repl.registerCommand(new SetDatabaseCommand(jtDatabase));
    repl.registerCommand(new LoadQuestionsCommand(jtDatabase));
  }

  /**
   * Starts the REPL.
   */
  public void start() {
    registerCommands();
    try {
      repl.start();
    } catch (IOException e) {
      System.out.println("ERROR: Input-Output exception from REPL");
    }
  }
}
