package edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.APIHandlers.JournalTexterREPL;

import edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.REPL.REPL;

import java.io.IOException;

public class JournalTexterREPL {
  private REPL repl;

  public JournalTexterREPL(REPL repl) {
    this.repl = repl;
  }
  public void registerCommands() {
    repl.registerCommand(new LoadQuestionsCommand());
  }

  public void start() {
    registerCommands();
    try {
      repl.start();
    } catch (IOException e) {
      System.out.println("ERROR: Input-Output exception from REPL");
    }
  }
}
