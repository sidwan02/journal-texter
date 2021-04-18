package edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.JournalTexterDB;

import java.util.Arrays;
import java.util.List;

/**
 * Utility class for creating a new database for JournalTexterDB.
 */
public final class JournalTexterDBCreation {
  /**
   * Private constructor.
   */
  private JournalTexterDBCreation() { }

  /**
   * Gets a list of create statements for the database used by JournalTexter.
   * @return a List of SQL create statements
   */
  public static List<String> tableCreateStatements() {
    return Arrays.asList(
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
  }
}
