package edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Utility class that handles several functionalities relating to SQL databases.
 */
public final class DbUtils {
  /**
   * Private constructor for utility class.
   */
  private DbUtils() {
  }

  /**
   * Closes a ResultSet and silences Exceptions.
   * @param rs the ResultSet to close
   */
  public static void closeQuietly(ResultSet rs) {
    if (rs != null) {
      try {
        rs.close();
      } catch (SQLException ignored) {
      }
    }
  }

  /**
   * Closes a PreparedStatement and silences Exceptions.
   * @param ps the PreparedStatement to close
   */
  public static void closeQuietly(PreparedStatement ps) {
    if (ps != null) {
      try {
        ps.close();
      } catch (SQLException ignored) {
      }
    }
  }

  /**
   * Closes a Connection and silences Exceptions.
   * @param conn the Connection to close
   */
  public static void closeQuietly(Connection conn) {
    if (conn != null) {
      try {
        conn.close();
      } catch (SQLException ignored) {
      }
    }
  }

  /**
   * Closes a ResultSet, PreparedStatement, and a Connection while silencing exceptions.
   * @param rs the ResultSet to close
   * @param ps the PreparedStatement to close
   * @param conn the Connection to close
   */
  public static void closeDatabaseObjectsQuietly(ResultSet rs, PreparedStatement ps,
                                                 Connection conn) {
    closeQuietly(rs);
    closeQuietly(ps);
    closeQuietly(conn);
  }

  /**
   * Closes a ResultSet, PreparedStatement, and a Connection while silencing exceptions.
   * @param ps the PreparedStatement to close
   * @param conn the Connection to close
   */
  public static void closeDatabaseObjectsQuietly(PreparedStatement ps,
                                                 Connection conn) {
    closeQuietly(ps);
    closeQuietly(conn);
  }
}
