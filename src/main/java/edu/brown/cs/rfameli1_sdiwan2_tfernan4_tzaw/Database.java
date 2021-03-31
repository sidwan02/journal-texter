package edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Class for managing a Connection to a SQLite3 database.
 */
public class Database {
  private final Connection conn;

  /**
   * Sets up a Connection to the provided SQLite3 database.
   *
   * @param filename The SQLite3 database to open
   * @throws SQLException if the provided file does not refer to a database or if another error occurs when connecting
   * @throws ClassNotFoundException should never happen, since SQLite class name is well-formed
   */
  public Database(String filename) throws SQLException, ClassNotFoundException {
    Class.forName("org.sqlite.JDBC");
    String urlToDB = "jdbc:sqlite:" + filename;
    conn = DriverManager.getConnection(urlToDB);
  }

  /**
   * Get the Connection to the database.
   * @return The Connection to the database.
   */
  public Connection getConn() {
    return conn;
  }
}
