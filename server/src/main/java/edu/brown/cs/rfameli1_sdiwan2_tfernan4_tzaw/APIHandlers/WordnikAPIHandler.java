package edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.APIHandlers;

import net.jeremybrooks.knicker.Knicker;
import net.jeremybrooks.knicker.KnickerException;
import net.jeremybrooks.knicker.WordApi;
import net.jeremybrooks.knicker.dto.Related;

import java.net.http.HttpHeaders;
import java.util.EnumSet;
import java.util.List;

public class WordnikAPIHandler {

  public void getSynonyms(String word) {
//    HttpHeaders header = new HttpHeaders();
//    HttpEntity<String> entity = new HttpEntity<String>("parameters", header);
//    "https://api.wordnik.com/v4/word.json/earth/relatedWords?useCanonical=false&relationshipTypes=synonym&limitPerRelationshipType=10&api_key=YOURAPIKEY"

    System.setProperty("WORDNIK_API_KEY", "70538348db6b42e43a5181e32070feebc0b303e293ed13a97");

    try {
      List<Related> list= WordApi.related(
        word,
        false,
        EnumSet.of(Knicker.RelationshipType.synonym),
        100);
      for(Related synonymCollection : list){
        for(String synonym : synonymCollection.getWords())
          System.out.println(synonym);
      }
    }
    catch (KnickerException e) {
      e.printStackTrace();
    }
  }
}
