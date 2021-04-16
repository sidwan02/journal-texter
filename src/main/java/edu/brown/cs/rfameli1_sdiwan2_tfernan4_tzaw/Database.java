package edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Class for managing a Connection to a SQLite3 database.
 */
public class Database {
  private final Connection conn;

  /**
   * Sets up a Connection to the provided SQLite3 database.
   *
   * @param filename The SQLite3 database to open
   * @throws SQLException if the provided file does not refer to a database or if another error
   * occurs when connecting
   * @throws ClassNotFoundException should never happen, since SQLite class name is well-formed
   */
  public Database(String filename) throws SQLException, ClassNotFoundException {
    Class.forName("org.sqlite.JDBC");
    String urlToDB = "jdbc:sqlite:" + filename;
    conn = DriverManager.getConnection(urlToDB);
    // these two lines tell the database to enforce foreign keys during operations.
    Statement stat = conn.createStatement();
    stat.executeUpdate("PRAGMA foreign_keys=ON;");
  }

  /**
   * Get the Connection to the database.
   * @return The Connection to the database.
   */
  public Connection getConnection() {
    return conn;
  }
}
