package edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.JournalTexterREPL;

import edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.Database.DatabaseCreator;
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

  // These are all the create statements for the tables required currently in JournalTexter
  private final List<String> tableCreateStatements = Arrays.asList(
      "CREATE TABLE \"entries\" ("
          + " \"id\" INTEGER NOT NULL UNIQUE,"
          + " \"date\" DATE NOT NULL,"
          + " \"entry_text\" TEXT NOT NULL,"
          + " \"author\" TEXT NOT NULL,"
          + " \"title\" TEXT NOT NULL,"
          + " FOREIGN KEY(\"author\") REFERENCES \"users\"(\"username\"),"
          + " PRIMARY KEY(\"id\" AUTOINCREMENT)"
          + ")",
      "CREATE TABLE \"entries_to_questions\" ( "
          + "\"entry_id\"  INTEGER, "
          + "\"question_id\"  INTEGER, "
          + "FOREIGN KEY (\"entry_id\") REFERENCES entries "
          + "FOREIGN KEY (\"question_id\") REFERENCES questions )",
      "CREATE TABLE \"questions\" ( "
          + "\"id\"  INTEGER NOT NULL UNIQUE, "
          + "\"text\"  TEXT UNIQUE, "
          + "PRIMARY KEY(\"id\" AUTOINCREMENT) )",
      "CREATE TABLE \"tags\" ( "
          + "\"id\"  INTEGER NOT NULL UNIQUE, "
          + "\"text\"  INTEGER NOT NULL, "
          + "PRIMARY KEY(\"id\" AUTOINCREMENT) )",
      "CREATE TABLE \"tags_to_entries\" ( "
          + "\"tag_id\"  INTEGER NOT NULL, "
          + "\"entry_id\"  INTEGER NOT NULL, "
          + "FOREIGN KEY(\"tag_id\") REFERENCES \"tags\" ON DELETE CASCADE "
          + "FOREIGN KEY(\"entry_id\") REFERENCES \"entries\" ON DELETE CASCADE )",
      "CREATE TABLE \"tags_to_questions\" ( "
          + "\"tag_id\"  INTEGER NOT NULL, "
          + "\"question_id\"  INTEGER NOT NULL, "
          + "FOREIGN KEY(\"tag_id\") REFERENCES \"tags\" ON DELETE CASCADE "
          + "FOREIGN KEY(\"question_id\") REFERENCES \"questions\" ON DELETE CASCADE )",
      "CREATE TABLE \"users\" ( "
          + "\"username\"  TEXT NOT NULL UNIQUE, "
          + "\"password\"  BLOB NOT NULL, "
          + "PRIMARY KEY(\"username\") )"
  );

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
      DatabaseCreator.createNewDatabase(filename, tableCreateStatements);
    } catch (Exception e) {
      System.out.println("ERROR: " + e.getMessage());
    }
  }
}
