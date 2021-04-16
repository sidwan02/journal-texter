package edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw;

import edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.Journal.Entry;
import edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.Journal.JournalText;
import edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.Journal.Question;
import edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.Spreadsheet.HeaderException;
import edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.Spreadsheet.InvalidFileException;
import edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.Spreadsheet.SpreadsheetData;
import edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.Spreadsheet.SpreadsheetReader;
import edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.utils.DateConversion;

import javax.security.auth.login.FailedLoginException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Handles all interactions with JournalTexter-related databases. Implements the Singleton design
 * pattern.
 */
public final class JournalTexterDB {
  private static final JournalTexterDB INSTANCE = new JournalTexterDB();
  private Connection conn = null;

  /**
   * Creates an instance of JournalTexterDB.
   */
  private JournalTexterDB() { }

  /**
   * Retrieves the current instance of JournalTexterDB.
   * @return the current instance of JournalTexterDB
   */
  public static JournalTexterDB getInstance() {
    return INSTANCE;
  }

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
      if (rows == null) {
        throw new InvalidFileException("Empty file passed into loadDataFromSpreadsheet");
      }
      PreparedStatement ps;
      ResultSet rs;

      for (List<String> r : rows) {
        // Check if the question already exists in the table
        String question = r.get(0);
        ps = conn.prepareStatement("SELECT * FROM questions WHERE text=?;");
        ps.setString(1, question);
        rs = ps.executeQuery();
        boolean questionAlreadyInTable = rs.next();
        // insert question if its not already in the table
        if (!questionAlreadyInTable) {
          ps = conn.prepareStatement("INSERT INTO questions (text) VALUES (?);");
          ps.setString(1, question);
          ps.executeUpdate();
        }
        // get the question's id
        ps = conn.prepareStatement("SELECT id FROM questions WHERE text=?;");
        ps.setString(1, question);
        rs = ps.executeQuery();
        int questionId = rs.getInt(1);

        // Get the tags from the second column of the spreadsheet
        String[] tags = r.get(1).split(",");
        for (String tag : tags) {
          // Check if the tag has been inserted in the tags table
          ps = conn.prepareStatement("SELECT * FROM tags WHERE text=?;");
          ps.setString(1, tag);
          rs = ps.executeQuery();
          // Insert the tag if it is not in the tag table
          if (!rs.next()) {
            ps = conn.prepareStatement("INSERT INTO tags (text) VALUES (?);");
            ps.setString(1, tag);
            ps.executeUpdate();
          }
          // get the tag's id
          ps = conn.prepareStatement("SELECT id FROM tags WHERE text=?;");
          ps.setString(1, tag);
          rs = ps.executeQuery();
          int tagId = rs.getInt(1);

          // Check if the tag-question relation already exists in the database
          ps = conn.prepareStatement("SELECT * FROM tags_to_questions "
              + "WHERE tag_id=? AND question_id=?");
          ps.setInt(1, tagId);
          ps.setInt(2, questionId);
          rs = ps.executeQuery();

          // insert tag-question relation if it does not exist already
          if (!rs.next()) {
            ps = conn.prepareStatement("INSERT INTO tags_to_questions VALUES (?, ?);");
            ps.setInt(1, tagId);
            ps.setInt(2, questionId);
            ps.executeUpdate();
          }
        }
      }
      return true;
    } catch (HeaderException | IOException | SQLException | InvalidFileException e) {
      System.out.println("ERROR: " + e.getMessage());
      e.printStackTrace();
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

    PreparedStatement ps = conn.prepareStatement("SELECT text FROM questions "
        + "WHERE id=(SELECT question_id FROM tags_to_questions "
        + "WHERE tag_id=(SELECT id FROM tags WHERE text=?))");
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
    List<Entry<JournalText>> entries = new ArrayList<>();
    while (rs.next()) {
      // May or may not need to use id / author in the future
      Integer id = rs.getInt(1);
      Date date = rs.getDate(2);
      String stringRepresentation = rs.getString(3);
      String author = rs.getString(4);
      // Get date into the LocalDate format
      LocalDate cleanedDate = Instant.ofEpochMilli(date.getTime())
          .atZone(ZoneId.systemDefault())
          .toLocalDate();
      entries.add(new Entry<>(id, cleanedDate, stringRepresentation));
    }
    return entries;
  }

  /**
   * Retrieves an entry from the database using its id.
   * @param entryId the id of the entry
   * @return an Entry object
   * @throws SQLException if the database connection has not been established or if an error occurs
   * with the
   */
  public Entry<JournalText> getEntryById(Integer entryId) throws SQLException {
    checkConnection();
    PreparedStatement ps = conn.prepareStatement("SELECT * FROM entries WHERE id=?");
    ps.setInt(1, entryId);
    ResultSet rs = ps.executeQuery();
    Integer id = rs.getInt(1);
    LocalDate date = DateConversion.dateToLocalDate(rs.getDate(2));
    String entryString = rs.getString(3);
    return new Entry<>(id, date, entryString);
  }

  /**
   * Creates a row within the entries table representing a new entry.
   * @param date the date the entry was created
   * @param entryText the formatted text to start the entry
   * @param username the author of the entry
   * @return the id of the created entry
   * @throws SQLException if connection has not been established or if an error occurs interacting
   * with the database
   */
  public Integer addUserEntry(LocalDate date, String entryText, String username)
      throws SQLException {
    checkConnection();
    Date sqlDate = java.sql.Date.valueOf(date);

    PreparedStatement ps = conn.prepareStatement("INSERT INTO entries "
        + "(date, entry_text, author) VALUES (?, ?, ?);");
    ps.setDate(1, sqlDate);
    ps.setString(2, entryText);
    ps.setString(3, username);
    ps.executeUpdate();

    // Retrieve the entry of the created id
    ps = conn.prepareStatement("SELECT last_insert_rowid();");
    ResultSet rs = ps.executeQuery();
    return rs.getInt(1);
  }

  /**
   * Updates an entry by adding new Questions and Responses.
   * @param entryId the id of the entry to update
   * @param toAdd the Questions and Responses to add
   * @throws SQLException if connection has not been established or if an error occurs interacting
   * with the database
   */
  public void addToEntry(Integer entryId, List<JournalText> toAdd) throws SQLException {
    checkConnection();

    PreparedStatement ps = conn.prepareStatement("SELECT entry_text FROM entries WHERE id=?");

    ps.setInt(1, entryId);

    ResultSet rs = ps.executeQuery();

    StringBuilder entryText = new StringBuilder();

    try {
      if (rs.next()) {
        if (rs.isClosed()) {
          System.out.println("rs is closed");
        }
        entryText = new StringBuilder(rs.getString(1));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    for (JournalText jt : toAdd) {
      entryText.append(jt.stringRepresentation());
    }

    ps = conn.prepareStatement("UPDATE entries SET entry_text=? WHERE id=?");

    ps.setString(1, entryText.toString());

    ps.setInt(2, entryId);

    ps.executeUpdate();

  }

  /**
   * Retrieves all tags from the tags table in the database.
   * @return a Set of all tags as Strings
   * @throws SQLException if connection has not been established or if an error occurs retrieving
   * from the database
   */
  public Set<String> getAllTagsFromDB() throws SQLException {
    checkConnection();
    PreparedStatement ps = conn.prepareStatement("SELECT text FROM tags;");
    ResultSet rs = ps.executeQuery();
    Set<String> allTags = new HashSet<>();
    while (rs.next()) {
      allTags.add(rs.getString(1));
    }
    return allTags;
  }



  /**
   * Checks if the input log in information correctly matches the database.
   * @param username the input username
   * @param inputPasswordBytes the input password as a byte array
   * @throws SQLException if the database connection has not been set up or if a SQL-related error
   * occurs
   * @throws FailedLoginException if the login credentials do not match those stored in the database
   */
  public void authenticateUser(String username, byte[] inputPasswordBytes)
      throws SQLException, FailedLoginException {
    checkConnection();

    PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE username=?;");
    ps.setString(1, username);
    ResultSet rs = ps.executeQuery();

    if (!rs.next()) {
      throw new FailedLoginException("No user found with username " + username);
    }
    byte[] registeredPasswordBytes = rs.getBytes(2);
    if (!Arrays.equals(inputPasswordBytes, registeredPasswordBytes)) {
      throw new FailedLoginException("Incorrect password");
    }
  }

  /**
   * Registers a new user into the database.
   * @param username the username to be registered
   * @param passwordBytes a byte array representing the password of the user
   * @throws SQLException if the database connection has not been established or if an error
   * occurs inserting into the database
   * @throws FailedLoginException if registering the user failed
   */
  public void registerUser(String username, byte[] passwordBytes)
      throws SQLException, FailedLoginException {
    checkConnection();
    // Check if the username has been registered already
    if (usernameIsRegistered(username)) {
      throw new FailedLoginException("Username " + username + " already registered");
    }
    PreparedStatement ps = conn.prepareStatement("INSERT INTO users VALUES (?, ?);");
    ps.setString(1, username);
    ps.setBytes(2, passwordBytes);
    ps.executeUpdate();
  }

  /**
   * Checks if the given username is registered in the database already.
   * @param username the username to check
   * @return true if the username is registered, else false
   * @throws SQLException if an error occurs with querying the SQL database
   */
  public boolean usernameIsRegistered(String username) throws SQLException {
    checkConnection();
    PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE username=?;");
    ps.setString(1, username);
    ResultSet rs = ps.executeQuery();
    return rs.next();
  }
}
