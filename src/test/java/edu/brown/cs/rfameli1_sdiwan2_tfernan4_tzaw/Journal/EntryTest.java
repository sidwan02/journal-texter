package edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.Journal;

import org.junit.Test;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import java.time.LocalDate;

import static org.junit.Assert.*;

public class EntryTest {

  Question question1 = new Question("How are you?");
  Question question2 = new Question("What's your favorite food?");
  Question emptyQuestion = new Question("");
  Response response1 = new Response("I am good");
  Response response2 = new Response("I am badasdadadadaaad");
  Response emptyResponse = new Response("");
  List<Question> questions = Arrays.asList(question1, emptyQuestion);
  List<Response> responses = Arrays.asList(response1, response2);
  List<JournalText> questionsResponses = Arrays.asList(question1, response1, emptyQuestion, response2);


  @Test
  public void testConstructorUsingString() {
    LocalDate date1 = LocalDate.now();
    Entry<JournalText> entry1 = new Entry<>(1, date1, "entry1",
        "{@Yo whats up}{not much}{I am good}{@How are you?}{@How are you?}{@}{}{I am good}",
        new LinkedList<>());
    assertEquals(date1, entry1.getDate());
    assertEquals(Optional.of(1), Optional.ofNullable(entry1.getId()));
    assertEquals("entry1", entry1.getTitle());
    List<JournalText> texts = entry1.getQuestionsAndResponses();
    assertEquals("Yo whats up", texts.get(0).getText());
    assertEquals("not much", texts.get(1).getText());
    assertEquals("I am good", texts.get(2).getText());
    assertEquals("How are you?", texts.get(3).getText());
    assertEquals("How are you?", texts.get(4).getText());
    assertEquals("", texts.get(5).getText());
    assertEquals("", texts.get(6).getText());
    assertEquals("I am good", texts.get(7).getText());
  }

  @Test
  public void testConstructorUsingList() {
    LocalDate date2 = LocalDate.now();
    Entry<JournalText> entry2 = new Entry<>(2, date2, "entry2", questionsResponses, new LinkedList<>());
    assertEquals(date2, entry2.getDate());
    assertEquals(Optional.of(2), Optional.ofNullable(entry2.getId()));
    assertEquals("entry2", entry2.getTitle());
    List<JournalText> texts = entry2.getQuestionsAndResponses();
    assertEquals("How are you?", texts.get(0).getText());
    assertEquals("I am good", texts.get(1).getText());
    assertEquals("", texts.get(2).getText());
    assertEquals("I am badasdadadadaaad", texts.get(3).getText());
  }
}
