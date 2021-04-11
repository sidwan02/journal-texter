package edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.JournalTexterREPL;

import edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.JournalTexterDB;
import edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.REPL.ArgHolder;
import edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.REPL.ArgTypes;
import edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.REPL.REPLCommand;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class ClearTableCommand implements REPLCommand {
  private JournalTexterDB jtDatabase;

  public ClearTableCommand(JournalTexterDB jtDatabase) {
    this.jtDatabase = jtDatabase;
  }

  @Override
  public String getCallWord() {
    return "clear";
  }

  @Override
  public List<List<ArgTypes>> getArgFormats() {
    return Collections.singletonList(Collections.singletonList(ArgTypes.STRING));
  }

  @Override
  public void run(ArgHolder arguments) {
    String tableName = arguments.nextString();
    String cantClearMessage = "ERROR: Table cannot be cleared: " + tableName;

    if (tableName.equals("users") || tableName.equals("entries")) {
      System.out.println(cantClearMessage);
    } else if (tableName.equals("questions") || tableName.equals("tags")) {
      try {
        jtDatabase.clearTable(tableName);
        System.out.println("Cleared table " + tableName);
      } catch (SQLException e) {
        System.out.println("ERROR: SQL Exception when trying to clear " + tableName);
      }
    } else {
      System.out.println("ERROR: No table found with name " + tableName);
    }
  }
}
