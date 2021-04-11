package edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.postRequestHandler;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.Journal.Question;
import edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.JournalTexterDB;
import edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.sentimentAnalysis.SentimentAnalysis;
import edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.wordCountVec.WordCountVec;
import org.json.JSONArray;
import org.json.JSONObject;
import spark.Request;
import spark.Response;
import spark.Route;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;

/**
 * postRequestHandler class to manage all handlers to the stars page.
 *
 */
public class GUIHandler {

  // for Riki: paste in methods required into each handler
  private final JournalTexterDB jtDatabase = JournalTexterDB.getInstance();

  public static class HandleRequestQuestion implements Route {
    private static final Gson GSON = new Gson();

    /**
     * Handles Axios requests from the javascript front-end and returns
     * the appropriate JSON object to be used by the front-end.
     *
     * @param request - request object for Axios request
     * @param response - response object for Axios request
     * @return a JSON object representing information to be used by the front end
     * @throws Exception if data cannot be accessed from given JSON object
     */
    @Override
    public Object handle(Request request, Response response) throws Exception {

      JSONObject data = new JSONObject(request.body());
      Map<String, Object> variables;

      String userNameOrUserID = data.getString("userID");
      JSONArray text = data.getJSONArray("text");
      String startState = data.getString("start"); // "true" -> on load / ""

      if (startState.equals("true")) {
        List<String> questions = getRandomlyGeneratedQuestions(5);

        variables = ImmutableMap.of(
          "questions", questions,
          "tags", new ArrayList<>(),
          "sentiment", -1);
      } else {
        List<String> responses = new ArrayList<>();
        if (text != null) {
          int len = text.length();
          for (int i=0;i<len;i++){
            responses.add(text.get(i).toString());
          }
        }

        WordCountVec vectorizor = new WordCountVec();

        String combinedResponses = String.join(" ", responses);

        Map<String, Integer> frequencies
          = vectorizor.getFrequenciesFromText(combinedResponses, 1);

        SortedSet<Map.Entry<String, Integer>> sortedFrequencies
          = vectorizor.sortByValues(frequencies);

        JournalTexterDB jtDB = new JournalTexterDB();
        Set<String> tags = jtDB.getAllTagsFromDB();

        List<String> foundTags = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : sortedFrequencies) {
          if (tags.contains(entry.getKey())) {
            foundTags.add(entry.getKey());
          }
        }

        List<String> questions = new ArrayList<>();
        for (String tag : foundTags) {
          List<Question> questionsFromTag = jtDB.findQuestionsFromTag(tag);
          for (Question q : questionsFromTag) {
            questions.add(q.getText());
            if (questions.size() >= 5) {
              break;
            }
          }
        }

        List<String> additionalQuestions
          = getRandomlyGeneratedQuestions(5 - questions.size());

        questions.addAll(additionalQuestions);

        SentimentAnalysis senti = new SentimentAnalysis();
        Double sentiment = senti.getSentimentFromText(combinedResponses);

        variables = ImmutableMap.of(
          "questions", questions,
          "tags", foundTags,
          "sentiment", sentiment);
      }

      /*-------*/
      /*
      You can get all the tags in the database using the jtDatabase getAllTagsFromDb method and
      find which ones apply using your vectorizor. Then you can find all questions that use those
      tags using findQuestionsFromTag (returns a List of Questions).
       */
      /*-------*/

      return GSON.toJson(variables);
    }

    public List<String> getRandomlyGeneratedQuestions(int n) {
      JournalTexterDB jtDB = new JournalTexterDB();
      Set<String> tags = jtDB.getAllTagsFromDB();

      List<String> randomlyChosenTags = new ArrayList<>();
      for (String tag : tags) {
        randomlyChosenTags.add(tag);
        if (randomlyChosenTags.size() == n) {
          break;
        }
      }

      List<String> questions = new ArrayList<>();
      for (String tag : randomlyChosenTags) {
        List<Question> questionsFromTag = jtDB.findQuestionsFromTag(tag);
        for (Question q : questionsFromTag) {
          questions.add(q.getText());
          if (questions.size() >= 5) {
            break;
          }
        }
      }
    }
  }

  public static class HandleSelectQuestion implements Route {
    private static final Gson GSON = new Gson();
    // Saves previous response list and new selected question, entry tags created here
    /**
     * Handles Axios requests from the javascript front-end and returns
     * the appropriate JSON object to be used by the front-end.
     *
     * @param request - request object for Axios request
     * @param response - response object for Axios request
     * @return a JSON object representing information to be used by the front end
     * @throws Exception if data cannot be accessed from given JSON object
     */
    @Override
    public Object handle(Request request, Response response) throws Exception {

      JSONObject data = new JSONObject(request.body());
      Map<String, Object> variables;

      Integer entryId = Integer.parseInt(data.getString("entryID"));
      String userNameOrUserID = data.getString("userID");
      String question = data.getString("question");
      // probably will not even need this, ideally from backend should be able to detect
      // the most recently saved entry and get that from SQL using a query

      /*-------*/
      /*
      When a user selects a question, that question should be added to the entry string. This would
      essentially be updating the entry in the database, and would have similar functionality
      to HandleSaveUserEntry. I can also easily create an appendToCurrentEntry method to add to
      the entry. This would require the entry's id, the username, and the text to be appended.
       */
      /*-------*/
      /*
      Question Handler saves question and previous response
       */

      WordCountVec vectorizor = new WordCountVec();
      variables = vectorizor.parseToGui();

      return GSON.toJson(variables);
    }
  }

  //public static class HandleCreateEntry implements Route {
    // Request ==>
    // Response ==> id of the entry that was created, store on the frontend and use it to request
    // specific entries from the backend
  //}

  public static class HandleClickSaveButton implements Route {
    private static final Gson GSON = new Gson();
    // Saves the last response list, entry tags created here
    /**
     * Handles Axios requests from the javascript front-end and returns
     * the appropriate JSON object to be used by the front-end.
     *
     * @param request - request object for Axios request
     * @param response - response object for Axios request
     * @return a JSON object representing information to be used by the front end
     * @throws Exception if data cannot be accessed from given JSON object
     */
    @Override
    public Object handle(Request request, Response response) throws Exception {

      JSONObject data = new JSONObject(request.body());
      Map<String, Object> variables;

      Integer entryId = Integer.parseInt(data.getString("entryID"));
      String userNameOrUserID = data.getString("userID");
      String text = data.getString("text");
      // probably will not even need this, ideally from backend should be able to detect
      // the most recently saved entry and get that from SQL using a query
      WordCountVec vectorizor = new WordCountVec();
      variables = vectorizor.parseToGui();
      /*-------*/
      /*
      Currently there is only a method to create a new entry row in the entries table.
      If we want to continually update a given entry, we would have to use its id to retrieve
      the specific entry.

      One way we can do this is by having separate handlers/methods/etc. for starting an entry
      and saving in an entry. For instance, on creation of an entry we would query the database
      to find the new entry's id and pass that to the frontend. Then, whenever we save the entry,
      we can pass the entry id as well so that Java knows which entry to update.

      Alternatively, if we ensure that each entry has a unique date, we could
      request and update a given entry that way.
       */
      /*-------*/
      return GSON.toJson(variables);
    }
  }

  public static class HandleUserHistorySummary implements Route {
    private static final Gson GSON = new Gson();

    /**
     * Handles Axios requests from the javascript front-end and returns
     * the appropriate JSON object to be used by the front-end.
     *
     * @param request - request object for Axios request
     * @param response - response object for Axios request
     * @return a JSON object representing information to be used by the front end
     * @throws Exception if data cannot be accessed from given JSON object
     */
    @Override
    public Object handle(Request request, Response response) throws Exception {

      JSONObject data = new JSONObject(request.body());
      Map<String, Object> variables;

      String userNameOrUserID = data.getString("userID");
      // probably will not even need this, ideally from backend should be able to detect
      // the most recently saved entry and get that from SQL using a query

      WordCountVec vectorizor = new WordCountVec();
      variables = vectorizor.parseToGui();
      /*-------*/
      /*
      You can get all of the entries from a user using getUserEntriesByUsername in JournalTexterDB.
      This will form instances of the Entry class from the string representation that you can work
      with.

      I can update the SQL database to have a new table that holds tags that relate to each entry
      so they can easily be retrieved.
       */
      /*-------*/

      return GSON.toJson(variables);
    }
  }

  public static class HandleUserHistoryRequest implements Route {
    private static final Gson GSON = new Gson();

    /**
     * Handles Axios requests from the javascript front-end and returns
     * the appropriate JSON object to be used by the front-end.
     *
     * @param request - request object for Axios request
     * @param response - response object for Axios request
     * @return a JSON object representing information to be used by the front end
     * @throws Exception if data cannot be accessed from given JSON object
     */
    @Override
    public Object handle(Request request, Response response) throws Exception {

      JSONObject data = new JSONObject(request.body());
      Map<String, Object> variables;
      Integer entryId = Integer.parseInt(data.getString("entryID"));
      String userNameOrUserID = data.getString("userID");
      String date = data.getString("date");
      // probably will not even need this, ideally from backend should be able to detect
      // the most recently saved entry and get that from SQL using a query

      WordCountVec vectorizor = new WordCountVec();
      variables = vectorizor.parseToGui();

      /*-------*/
      /*
      Assuming this relates to seeing all the text of the entry after a user pulls it up, this can
      be done by sending the id of the entry. I can create a method to retrieve the entry based
      on its id, or all entries from a given date.
       */
      /*-------*/

      return GSON.toJson(variables);
    }
  }
}
