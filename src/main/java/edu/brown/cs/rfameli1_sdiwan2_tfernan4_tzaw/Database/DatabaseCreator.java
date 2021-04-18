package edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.Database;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * Class for creating new databases.
 */
public final class DatabaseCreator {

  private DatabaseCreator() { }

  /**
   * Creates a new database file with the tables specified by the tableCreationStatements.
   * @param filename the name of the file to be created, including its filepath within the directory
   * @param tableCreationStatements a List of SQL CREATE statements for creating all necessary
   *                                tables
   * @throws IOException if the filename is invalid, the file already exists, or the file could not
   * be created
   * @throws SQLException if an error occurs creating the database or accessing the connection
   * @throws ClassNotFoundException if a class could not be found during creation
   */
  public static void createNewDatabase(String filename, List<String> tableCreationStatements)
      throws IOException, SQLException, ClassNotFoundException {
    File file = new File(filename);
    // Check conditions: ends with .db, file does not already exist, file can be created
    if (!filename.startsWith(".db", filename.length() - 3)) {
      throw new IOException("Database file requires .db file ending");
    }
    if (file.exists()) {
      throw new IOException("File " + filename + " already exists.");
    }
    if (!file.createNewFile()) {
      throw new IOException("File " + filename + " could not be created");
    }
    // Create the database connection
    Database db = new Database(filename);
    Connection conn = db.getConnection();

    // Create all the tables
    createAllTables(tableCreationStatements, conn);

    // Close the connection
    DbUtils.closeQuietly(conn);
  }

  /**
   * Creates all tables using the provided list of create statements.
   * @param createStatements a List of SQL statements for creating tables
   * @param conn the database connection to be used
   * @throws SQLException if a database access error occurs or if one of the create statements fail
   */
  private static void createAllTables(List<String> createStatements, Connection conn)
      throws SQLException {
    for (String createStatement : createStatements) {
      PreparedStatement ps = conn.prepareStatement(createStatement);
      ps.executeUpdate();
      DbUtils.closeQuietly(ps);
    }
  }
}
