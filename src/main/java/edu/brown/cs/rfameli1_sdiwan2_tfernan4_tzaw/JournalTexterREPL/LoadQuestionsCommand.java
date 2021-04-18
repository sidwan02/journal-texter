package edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.JournalTexterREPL;

import edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.JournalTexterDB.JournalTexterDB;
import edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.REPL.ArgHolder;
import edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.REPL.ArgTypes;
import edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.REPL.REPLCommand;
import java.util.Collections;
import java.util.List;

/**
 * Command class for loading questions into the database.
 */
public class LoadQuestionsCommand implements REPLCommand {

  private final JournalTexterDB jtDatabase;

  /**
   * Constructs the command.
   * @param jtDatabase the database that will be used
   */
  public LoadQuestionsCommand(JournalTexterDB jtDatabase) {
    this.jtDatabase = jtDatabase;
  }


  @Override
  public String getCallWord() {
    return "load";
  }

  @Override
  public List<List<ArgTypes>> getArgFormats() {
    return Collections.singletonList(Collections.singletonList(ArgTypes.STRING));
  }

  @Override
  public void run(ArgHolder arguments) {
    String spreadsheetFile = arguments.nextString();
    if (jtDatabase.loadQuestionsAndTagsFromSheet(spreadsheetFile)) {
      System.out.println("Successfully loaded in questions");
    }
  }
}
