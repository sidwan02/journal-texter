package edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class QuestionsDatabase {
  private Connection conn;
  public QuestionsDatabase(Connection conn) {
    this.conn = conn;
  }

  public List<Question> findQuestionsFromTag(String t) throws SQLException {
    PreparedStatement ps = conn.prepareStatement("SELECT ");
  }

}
