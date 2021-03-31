package java.edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw;

import java.sql.Connection;
import java.util.List;

public class QuestionsDatabase {
  private Connection conn;
  public QuestionsDatabase(Connection conn) {
    this.conn = conn;
  }
  public List<Question> findQuestionsFromTag(Tag t) {
  }

}
