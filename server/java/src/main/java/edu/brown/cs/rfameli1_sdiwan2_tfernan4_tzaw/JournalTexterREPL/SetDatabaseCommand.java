package edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.JournalTexterREPL;

import edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.Database;
import edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.JournalTexterDB;
import edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.REPL.ArgHolder;
import edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.REPL.ArgTypes;
import edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.REPL.REPLCommand;

import java.io.File;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class SetDatabaseCommand implements REPLCommand {
  private final JournalTexterDB jtDatabase;

  public SetDatabaseCommand(JournalTexterDB jtDatabase) {
    this.jtDatabase = jtDatabase;
  }

  @Override
  public String getCallWord() {
    return "database";
  }

  @Override
  public List<List<ArgTypes>> getArgFormats() {
    return Collections.singletonList(Collections.singletonList(ArgTypes.STRING));
  }

  @Override
  public void run(ArgHolder arguments) {
    String databaseName = arguments.nextString();
    try {
      // Validate the Database File Exists
      File file = new File(databaseName);
      if (!file.exists()) {
        System.out.println("ERROR: Database file " + databaseName + " does not exist");
      } else {
        // Load connection and validate database.
        Database questionsDB = new Database(databaseName);
        jtDatabase.setConnection(questionsDB.getConnection());
        System.out.println("Database connection set to " + databaseName);
      }
    } catch (SQLException e) {
      System.out.println("ERROR: Invalid connection to database " + databaseName);
    } catch (ClassNotFoundException e) {
      System.out.println("ERROR: Class not found exception");
    }

  }
}