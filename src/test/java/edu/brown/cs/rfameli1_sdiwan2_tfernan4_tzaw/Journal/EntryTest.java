package edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.Journal;

import org.junit.Test;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
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


  @Test
  public void testConstructorsAndGetters() {
    LocalDate date1 = LocalDate.now();
    Entry<JournalText> entry1 = new Entry<>(1, LocalDate.now(),
        "{@Yo whats up}{not much}{I am good}{@How are you?}{@How are you?}{@}{}{I am good}");
    assertEquals(Optional.of(1), Optional.ofNullable(entry1.getId()));
    
  }


  @Test
  public void testGetters() {

  }



}
