package edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.JournalTexterREPL;

import edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.Database.DatabaseCreator;
import edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.JournalTexterDB.JournalTexterDBCreation;
import edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.REPL.ArgHolder;
import edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.REPL.ArgTypes;
import edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.REPL.REPLCommand;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * REPLCommand class used to create a new database for the JournalTexter app.
 */
public class NewDatabaseCommand implements REPLCommand {
  /**
   * Creates a new instance of NewDatabaseCommand.
   */
  public NewDatabaseCommand() {
  }

  @Override
  public String getCallWord() {
    return "newdb";
  }

  @Override
  public List<List<ArgTypes>> getArgFormats() {
    return Collections.singletonList(Collections.singletonList(ArgTypes.STRING));
  }

  @Override
  public void run(ArgHolder arguments) {
    String filename = arguments.nextString();
    try {
      DatabaseCreator.createNewDatabase(filename,
          JournalTexterDBCreation.tableCreateStatements());
      System.out.println("Created new database file " + filename);
    } catch (Exception e) {
      System.out.println("ERROR: " + e.getMessage());
    }
  }
}
