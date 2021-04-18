package edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw;

import edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.Database.Database;
import edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.Database.DatabaseCreator;
import edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.Database.DbUtils;
import edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.JournalTexterDB.JournalTexterDBCreation;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * Class used for testing database methods.
 */
public class DatabaseFunctionTester {
  private static Connection conn;

  /**
   * Constructor.
   */
  private DatabaseFunctionTester() { }



//
//  /**
//   * Sets an empty database for use in tests.
//   * @param filename the name of the file to create the database
//   * @return a Connection to the created database
//   * @throws IOException if an error occurs with the file
//   * @throws SQLException if a faulty database connection occurs or a SQL statement fails
//   * @throws ClassNotFoundException if a class is not found during creation
//   */
//  public static Connection setEmptyDatabaseAndGetConn(String filename)
//      throws IOException, SQLException, ClassNotFoundException {
//    deleteTestDatabaseIfExists(filename);
//    DatabaseCreator.createNewDatabase(filename, JournalTexterDBCreation.tableCreateStatements());
//    Database db = new Database(filename);
//    conn = db.getConnection();
//    createAllTables(conn);
//    return conn;
//  }

  //  /**
//   * Drops all JournalTexter-related tables from the database.
//   * @param conn
//   * @throws SQLException
//   */
//  public static void dropAllTables(Connection conn) throws SQLException {
//    List<String> tableNames = Arrays.asList(
//        "entries_to_questions", "tags_to_entries", "tags_to_questions",
//        "users", "questions", "tags", "entries");
//    for (String table : tableNames) {
//      dropTable(conn, table);
//    }
//  }
//
//  /**
//   * Drops the given table in the
//   * @param tableName the name of the table to clear
//   * @throws SQLException if no connection has been established or if an error occurs with the SQL
//   * update
//   */
//  public static void dropTable(Connection conn, String tableName) throws SQLException {
//    // BUG HERE
//    PreparedStatement ps = conn.prepareStatement("DROP TABLE IF EXISTS ?");
//    ps.setString(1, tableName);
//    ps.executeUpdate();
//    DbUtils.closeQuietly(ps);
//  }

}
