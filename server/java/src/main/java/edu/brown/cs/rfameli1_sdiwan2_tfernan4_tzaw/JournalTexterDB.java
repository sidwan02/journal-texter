package edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw;

import edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.Journal.Entry;
import edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.Journal.JournalText;
import edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.Journal.Question;
import edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.Spreadsheet.HeaderException;
import edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.Spreadsheet.SpreadsheetData;
import edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.Spreadsheet.SpreadsheetReader;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Handles all interactions with JournalTexter-related databases.
 */
public class JournalTexterDB {
  private Connection conn = null;

  /**
   * Creates an instance of JournalTexterDB
   */
  public JournalTexterDB() { }

  /**
   * Sets the database connection to the specified connection.
   * @param connection a Connection to a database
   */
  public void setConnection(Connection connection) {
    this.conn = connection;
  }

  /**
   * Gets the current database connection.
   * @return a Connection for the current database
   */
  public Connection getConnection() {
    return this.conn;
  }

  /**
   * Checks that a connection to a database has been established.
   * @throws SQLException if there has been no connection set up
   */
  public void checkConnection() throws SQLException {
    if (conn == null) {
      throw new SQLException("Database connection has not been set up in JournalTexter Database");
    }
  }

  /**
   * Clears a given table in the current database. Only works for questions and tags tables.
   * @param tableName the name of the table to clear
   * @throws SQLException if no connection has been established or if an error occurs with the SQL
   * update
   */
  public void clearTable(String tableName) throws SQLException {
    checkConnection();
    if (tableName.equals("questions") || tableName.equals("tags")) {
      PreparedStatement ps = conn.prepareStatement("DELETE FROM ?");
      ps.setString(1, tableName);
      ps.executeUpdate();
    }
  }

  /**
   * Loads all the questions and tags from a spreadsheet into the questions and tags tables in the
   * current database.
   * @param filename the name of the spreadsheet file to be read from (must be in .tsv format)
   * @return true if the data was successfully loaded, else false
   */
  public boolean loadDataFromSpreadsheet(String filename) {
    try {
      SpreadsheetData sd = SpreadsheetReader.parseSpreadsheet(filename, "\t",
          Arrays.asList("Question", "Tags"));
      List<List<String>> rows = sd.getRows();
      PreparedStatement ps;
      ResultSet rs;

      for (List<String> r : rows) {
        // Check if the question already exists in the table
        String question = r.get(0);
        ps = conn.prepareStatement("SELECT * FROM questions WHERE question_text=?;");
        ps.setString(1, question);
        rs = ps.executeQuery();
        boolean questionAlreadyInTable = rs.next();
        // insert question if its not already in the table
        if (!questionAlreadyInTable) {
          ps = conn.prepareStatement("INSERT INTO questions (question_text) VALUES (?);");
          ps.setString(1, question);
          ps.executeUpdate();
        }
        // Get the tags from the second column of the spreadsheet
        String[] tags = r.get(1).split(",");
        for (String tag : tags) {
          // Check if the tag-question relation already exists in the table
          ps = conn.prepareStatement("SELECT * FROM tags WHERE tag_text=? AND "
              + "question_id=(SELECT id FROM questions WHERE question_text=?);");
          ps.setString(1, tag);
          ps.setString(2, question);
          rs = ps.executeQuery();
          // insert tag if the tag-question relation does not already exist
          if (!rs.next()) {
            ps = conn.prepareStatement("INSERT INTO tags (tag_text, question_id) VALUES (?, "
                + "(SELECT id FROM questions WHERE question_text=?));");
            ps.setString(1, tag);
            ps.setString(2, question);
            ps.executeUpdate();
          }
        }
      }
      return true;
    } catch (HeaderException | IOException | SQLException e) {
      System.out.println(e.getMessage());
      return false;
    }
  }

  /**
   * Finds all related questions from a tag in the database.
   * @param tag a String representing the tag
   * @return a List of all Questions that contain the given tag
   * @throws SQLException if the connection has not been established or if an error occurs with
   * a SQL statement
   */
  public List<Question> findQuestionsFromTag(String tag) throws SQLException {
    checkConnection();
    List<Question> questions = new ArrayList<>();

    PreparedStatement ps = conn.prepareStatement("SELECT question_text FROM questions "
        + "WHERE id=(SELECT question_id FROM tags WHERE tag_text=?)");
    ps.setString(1, tag);
    ResultSet questionsResults = ps.executeQuery();
    while (questionsResults.next()) {
      String questionText = questionsResults.getString(1);
      questions.add(new Question(questionText));
    }
    return questions;
  }

  /**
   * Gets all the entries of a user by using their username.
   * @param username the user's unique username
   * @return a List of entries as instances of the Entry class
   * @throws SQLException if the connection has not been established or if an error occurs with one
   * of the SQL statements
   */
  public List<Entry<JournalText>> getUserEntriesByUsername(String username) throws SQLException {
    checkConnection();
    PreparedStatement ps = conn.prepareStatement("SELECT * FROM entries WHERE author=?");
    ps.setString(1, username);
    ResultSet rs = ps.executeQuery();
    while (rs.next()) {
      Integer id = rs.getInt(1);
      Date date = rs.getDate(2);
      String text = rs.getString(3);
      String author = rs.getString(4);

      // TODO make this into Entrys
    }
    return null;
  }

  /**
   * Retrieves all tags from the tags table in the database.
   * @return a Set of all tags as Strings
   * @throws SQLException if connection has not been established or if an error occurs retrieving
   * from the database
   */
  public Set<String> getAllTagsFromDB() throws SQLException {
    checkConnection();
    PreparedStatement ps = conn.prepareStatement("SELECT DISTINCT tag_text FROM tags;");
    ResultSet rs = ps.executeQuery();
    Set<String> allTags = new HashSet<>();
    while (rs.next()) {
      allTags.add(rs.getString(1));
    }
    return allTags;
  }

  public void addUserEntry(LocalDate date, String entryText, String username) throws SQLException {
    checkConnection();
    PreparedStatement ps = conn.prepareStatement("INSERT INTO entries "
        + "(date, entry_text, author) VALUES (?, ?, ?);");
    ps.setDate(1, java.sql.Date.valueOf(date));
    ps.setString(2, entryText);
    ps.setString(3, username);
    ps.executeUpdate();
  }

  public void registerUser(String username, String password) throws SQLException {
    checkConnection();

    if (usernameIsRegistered(username)) {
      throw new SQLException("Username" + username + "already registered");
    }
    PreparedStatement ps = conn.prepareStatement("INSERT INTO users VALUES (?, ?);");
    ps.setString(1, username);
    ps.setString(2, password);
    ps.executeUpdate();
  }

  public boolean usernameIsRegistered(String username) throws SQLException {
    checkConnection();
    PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE username=?;");
    ps.setString(1, username);
    ResultSet rs = ps.executeQuery();
    if (rs.next()) {
      return true;
    }
    return false;
  }
}