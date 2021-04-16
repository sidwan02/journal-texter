package edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw;

import edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.Database.Database;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseFunctionTester {
  private DatabaseFunctionTester() { }
  private static Connection conn = null;

  public static Connection getConnection() {
    return conn;
  }


  public static boolean createEmptyDatabase(String filename)
      throws IOException, SQLException, ClassNotFoundException {
    File newDb = new File(filename);
    deleteFileIfExists(filename);
    if (!newDb.createNewFile()) {
      return false;
    }
    Database db = new Database(filename);
    conn = db.getConnection();
    loadAllTables(db.getConnection());
    return true;
  }

  private static void loadAllTables(Connection conn) throws SQLException {
    PreparedStatement ps = conn.prepareStatement(
        "CREATE TABLE \"entries\" ( " +
        "  \"id\"  INTEGER NOT NULL UNIQUE, " +
        "  \"date\"  DATE, " +
        "  \"entry_text\"  TEXT, " +
        "  \"author\"  TEXT, " +
        "  FOREIGN KEY(\"author\") REFERENCES \"users\"(\"username\"), " +
        "  PRIMARY KEY(\"id\" AUTOINCREMENT) " +
        ")");
    ps.executeUpdate();

    ps = conn.prepareStatement(
        "CREATE TABLE \"entries_to_questions\" ( " +
            "  \"entry_id\"  INTEGER, " +
            "  \"question_id\"  INTEGER, " +
            "  FOREIGN KEY (\"entry_id\") REFERENCES entries " +
            "  FOREIGN KEY (\"question_id\") REFERENCES questions " +
            ")"
    );
    ps.executeUpdate();

    ps = conn.prepareStatement(
        "CREATE TABLE \"questions\" ( " +
            "  \"id\"  INTEGER NOT NULL UNIQUE, " +
            "  \"text\"  TEXT UNIQUE, " +
            "  PRIMARY KEY(\"id\" AUTOINCREMENT) " +
            ")"
    );
    ps.executeUpdate();

    ps = conn.prepareStatement(
        "CREATE TABLE \"tags\" ( " +
            "  \"id\"  INTEGER NOT NULL UNIQUE, " +
            "  \"text\"  INTEGER NOT NULL, " +
            "  PRIMARY KEY(\"id\" AUTOINCREMENT) " +
            ")"
    );
    ps.executeUpdate();

    ps = conn.prepareStatement(
        "CREATE TABLE \"tags_to_entries\" ( " +
            "  \"tag_id\"  INTEGER NOT NULL, " +
            "  \"entry_id\"  INTEGER NOT NULL, " +
            "  FOREIGN KEY(\"tag_id\") REFERENCES \"tags\" ON DELETE CASCADE " +
            "  FOREIGN KEY(\"entry_id\") REFERENCES \"entries\" ON DELETE CASCADE " +
            ")"
    );
    ps.executeUpdate();

    ps = conn.prepareStatement(
        "CREATE TABLE \"tags_to_questions\" ( " +
            "  \"tag_id\"  INTEGER NOT NULL, " +
            "  \"question_id\"  INTEGER NOT NULL, " +
            "  FOREIGN KEY(\"tag_id\") REFERENCES \"tags\" ON DELETE CASCADE " +
            "  FOREIGN KEY(\"question_id\") REFERENCES \"questions\" ON DELETE CASCADE " +
            ")"
    );
    ps.executeUpdate();

    ps = conn.prepareStatement(
        "CREATE TABLE \"users\" ( " +
            "  \"username\"  TEXT NOT NULL UNIQUE, " +
            "  \"password\"  BLOB NOT NULL, " +
            "  PRIMARY KEY(\"username\") " +
            ")"
    );
    ps.executeUpdate();
  }

  public static void deleteFileIfExists(String filename) throws IOException {
    File f = new File(filename);
    if (f.exists()) {
      if (!f.delete()) {
        throw new IOException("File " + filename + " could not be deleted in DatabaseGenerator.deleteDatabaseFile");
      }
    }
  }

  /**
   * Clears a given table in the current database. Only works for questions and tags tables.
   * @param tableName the name of the table to clear
   * @throws SQLException if no connection has been established or if an error occurs with the SQL
   * update
   */
  public static void clearTable(Connection conn, String tableName) throws SQLException {
    if (tableName.equals("questions") || tableName.equals("tags")) {
      PreparedStatement ps = conn.prepareStatement("DELETE FROM ?");
      ps.setString(1, tableName);
      ps.executeUpdate();
    }
  }
}
