package edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw;

import edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.Database.Database;
import edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.Database.DatabaseCreator;
import edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.Database.DbUtils;
import edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.Journal.Entry;
import edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.Journal.JournalText;
import edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.Journal.Question;
import edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.Journal.Response;
import edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.JournalTexterDB.JournalTexterDB;
import edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.JournalTexterDB.JournalTexterDBCreation;
import org.junit.Test;
import org.junit.After;
import org.junit.Before;

import javax.security.auth.login.FailedLoginException;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class JournalTexterDBTest {
  private JournalTexterDB jtDb = null;
  private Connection conn = null;
  private PreparedStatement ps;
  private ResultSet rs;
  private final String testDatabaseFileName = "data/test-database.db";

  Question question1 = new Question("How are you?");
  Question emptyQuestion = new Question("");
  Response response1 = new Response("I am good");
  Response emptyResponse = new Response("");

  private void deleteTestFileIfExists() throws IOException {
    File file = new File(testDatabaseFileName);
    if (file.exists()) {
      if (!file.delete()) {
        throw new IOException("ERROR: Could not delete file "
            + testDatabaseFileName + " in JournalTexterDBTest");
      }
    }
  }


  @Before
  public void setUp() throws SQLException, IOException, ClassNotFoundException {
    jtDb = JournalTexterDB.getInstance();
    // Delete the current test database file if it exists
    deleteTestFileIfExists();
    DatabaseCreator.createNewDatabase(
        testDatabaseFileName, JournalTexterDBCreation.tableCreateStatements());
    Database db = new Database(testDatabaseFileName);
    conn = db.getConnection();
    jtDb.setConnection(conn);
  }

  @After
  public void tearDown() throws IOException {
    DbUtils.closeQuietly(conn);
    jtDb.setConnection(null);
    conn = null;
    deleteTestFileIfExists();
  }

  @Test
  public void testCreateAndAddToEntry()
      throws SQLException, FailedLoginException {
    jtDb.registerUser("riki", "riki".getBytes(StandardCharsets.UTF_8));
    int entryId = jtDb.addUserEntry(LocalDate.now(), "{@Yo whats up}{not much}", "riki");
    assertEquals(1, entryId);
    entryId = jtDb.addUserEntry(LocalDate.now(), "", "riki");
    assertEquals(2, entryId);
    ps = conn.prepareStatement("SELECT * FROM entries WHERE id=1");
    rs = ps.executeQuery();
    assertEquals("{@Yo whats up}{not much}", rs.getString(3));
    assertEquals("riki", rs.getString(4));

    // Add a response
    jtDb.addToEntry(1, Collections.singletonList(response1), new ArrayList<>());
    ps = conn.prepareStatement("SELECT * FROM entries WHERE id=1");
    rs = ps.executeQuery();
    assertEquals("{@Yo whats up}{not much}{I am good}", rs.getString(3));
    assertEquals("{@Yo whats up}{not much}{I am good}", jtDb.getEntryById(1).getString());

    // Add a question
    jtDb.addToEntry(1, Collections.singletonList(question1), new ArrayList<>());
    ps = conn.prepareStatement("SELECT * FROM entries WHERE id=1");
    rs = ps.executeQuery();
    assertEquals("{@Yo whats up}{not much}{I am good}{@How are you?}", rs.getString(3));
    assertEquals("{@Yo whats up}{not much}{I am good}{@How are you?}", jtDb.getEntryById(1).getString());

    // Add a question and response
    jtDb.addToEntry(1, Arrays.asList(question1, emptyQuestion, emptyResponse, response1), new ArrayList<>());
    ps = conn.prepareStatement("SELECT * FROM entries WHERE id=1");
    rs = ps.executeQuery();
    assertEquals("{@Yo whats up}{not much}{I am good}{@How are you?}{@How are you?}{@}{}{I am good}", rs.getString(3));
    assertEquals("{@Yo whats up}{not much}{I am good}{@How are you?}{@How are you?}{@}{}{I am good}", jtDb.getEntryById(1).getString());

    // Delete entry
    jtDb.deleteEntry(1);
    ps = conn.prepareStatement("SELECT * FROM entries WHERE id=1");
    rs = ps.executeQuery();
    assertFalse(rs.next());

    // Clear entry
    jtDb.resetEntryText(2);
    ps = conn.prepareStatement("SELECT entry_text FROM entries WHERE id=2");
    rs = ps.executeQuery();
    while (rs.next()) {
      assertEquals(rs.getString(1), "");
    }
    DbUtils.closeResultSetAndPrepStatement(rs, ps);
  }

  @Test
  public void testGetEntriesFromDatabase()
      throws SQLException, FailedLoginException {
    jtDb.registerUser("siddy", "siddy".getBytes(StandardCharsets.UTF_8));
    jtDb.registerUser("riddy", "riddy".getBytes(StandardCharsets.UTF_8));
    ps = conn.prepareStatement("SELECT * FROM entries WHERE id=1");
    rs = ps.executeQuery();
    assertFalse(rs.next());

    LocalDate d = LocalDate.now();
    String siddyEntryText = "{@How are you?}{apple}";
    jtDb.addUserEntry(d, siddyEntryText, "siddy");
    List<Entry<JournalText>> riddyEntries = jtDb.getUserEntriesByUsername("riddy");
    assertEquals(Collections.emptyList(), riddyEntries);

    Entry<JournalText> siddyEntry = jtDb.getUserEntriesByUsername("siddy").get(0);
    assertEquals(siddyEntryText, siddyEntry.getString());
    assertEquals(siddyEntryText, jtDb.getEntryById(1).getString());
  }

  @Test
  public void testQuestionAndTagMethods() {




    // TODO add a test questions sheet to load in data from
  }

  @Test
  public void testUserMethods() throws FailedLoginException, SQLException {
    byte[] siddyPassword = "siddy".getBytes(StandardCharsets.UTF_8);

    jtDb.registerUser("siddy", siddyPassword);
    jtDb.authenticateUser("siddy", siddyPassword);
    assertThrows(FailedLoginException.class,
        () -> jtDb.authenticateUser("siddy", "soddy".getBytes(StandardCharsets.UTF_8)));
    assertThrows(FailedLoginException.class,
        () -> jtDb.authenticateUser("soddy", "siddy".getBytes(StandardCharsets.UTF_8)));
  }


}
