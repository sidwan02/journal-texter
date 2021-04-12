package edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.postRequestHandler;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.Journal.Entry;
import edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.Journal.JournalText;
import edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.Journal.JournalTextType;
import edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.Journal.Question;
import edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.JournalTexterDB;
import edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.wordCountVec.WordCountVec;
import org.json.JSONArray;
import org.json.JSONObject;
import spark.Request;
import spark.Response;
import spark.Route;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * postRequestHandler class to manage all handlers to the stars page.
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
     * @param request  - request object for Axios request
     * @param response - response object for Axios request
     * @return a JSON object representing information to be used by the front end
     * @throws Exception if data cannot be accessed from given JSON object
     */
    @Override
    public Object handle(Request request, Response response) throws Exception {
      // If state requestQuestions Saves previous response list and new selected question, entry tags created here
      // If state start just gives questions

      JSONObject data = new JSONObject(request.body());
      Map<String, Object> variables;

      // id of the entry
      Integer entryId = Integer.parseInt(data.getString("entryID"));

      // username
      String username = data.getString("userID");
      // list of responses in the from of a JSON array
      JSONArray text = data.getJSONArray("text");
      // either "start" or "requestQuestion"
      // "start" -> when loading the first question of an entry
      // "requestQuestion" -> when loading a question based on user responses to a previous question
      String state = data.getString("state");

      if (state.equals("start")) {
        List<String> questions = BackendConnection.getRandomlyGeneratedQuestions(1);

        List<JournalText> entryInfo = new ArrayList<>();
        entryInfo.add(new Question(questions.get(0)));

        JournalTexterDB jtDB = new JournalTexterDB();
        jtDB.addToEntry(entryId, entryInfo);

        variables = ImmutableMap.of(
            "questions", questions,
            "tags", new ArrayList<>(),
            "sentiment", -1);
      } else if (state.equals("requestQuestion")) {
        List<String> responses = new ArrayList<>();
        if (text != null) {
          int len = text.length();
          for (int i = 0; i < len; i++) {
            responses.add(text.get(i).toString());
          }
        }

        String combinedResponses = String.join(" ", responses);

        Set<String> foundTags = BackendConnection.getTagsFromResponses(combinedResponses);

        List<String> questions = BackendConnection.getQuestionsFromTags(foundTags);

        List<String> additionalQuestions
            = BackendConnection.getRandomlyGeneratedQuestions(5 - questions.size());

        questions.addAll(additionalQuestions);

        // TODO: Deal with sentiment
        double sentiment = -1.0;
        //double sentiment = BackendConnection.getSentimentFromResponses(combinedResponses);

        variables = ImmutableMap.of(
            "questions", questions,
            "tags", foundTags,
            "sentiment", sentiment);
      } else {
        variables = ImmutableMap.of(
            "questions", new ArrayList<>(),
            "tags", new ArrayList<>(),
            "sentiment", -1);
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
  }

  public static class HandleSaveUserInputs implements Route {
    private static final Gson GSON = new Gson();

    /**
     * Handles Axios requests from the javascript front-end and returns
     * the appropriate JSON object to be used by the front-end.
     *
     * @param request  - request object for Axios request
     * @param response - response object for Axios request
     * @return a JSON object representing information to be used by the front end
     * @throws Exception if data cannot be accessed from given JSON object
     */
    @Override
    public Object handle(Request request, Response response) throws Exception {
      // If state requestQuestions Saves previous response list and new selected question, entry tags created here
      // If state start just gives questions

      JSONObject data = new JSONObject(request.body());
      Map<String, Object> variables;

      // id of the entry
      Integer entryId = Integer.parseInt(data.getString("entryID"));
      // question selected by user
      String question = data.getString("question");
      // username
      String username = data.getString("userID");
      // list of responses in the form of a JSON array
      JSONArray text = data.getJSONArray("text");
      // either "saveQuestion" or "saveEntry"
      // "saveQuestion" -> after the user clicks on a new question
      // "saveEntry" -> after the user clicks on the save entry btn
      String state = data.getString("state");

      if (state.equals("saveQuestion")) {
        List<String> responses = new ArrayList<>();
        if (text != null) {
          int len = text.length();
          for (int i = 0; i < len; i++) {
            responses.add(text.get(i).toString());
          }
        }

        String combinedResponses = String.join(" ", responses);

        Set<String> foundTags = BackendConnection.getTagsFromResponses(combinedResponses);

        double sentiment = BackendConnection.getSentimentFromResponses(combinedResponses);

        variables = ImmutableMap.of(
            "tags", foundTags,
            "sentiment", sentiment);
      } else if (state.equals("saveEntry")) {
        List<String> responses = new ArrayList<>();
        if (text != null) {
          int len = text.length();
          for (int i = 0; i < len; i++) {
            responses.add(text.get(i).toString());
          }
        }

        String combinedResponses = String.join(" ", responses);

        Set<String> foundTags = BackendConnection.getTagsFromResponses(combinedResponses);

        double sentiment = BackendConnection.getSentimentFromResponses(combinedResponses);

        List<JournalText> entryInfo = new ArrayList<>();
        for (String res : responses) {
          entryInfo.add(new edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.Journal.Response(res));
        }

        entryInfo.add(new Question(question));

        JournalTexterDB jtDB = new JournalTexterDB();
        jtDB.addToEntry(entryId, entryInfo);

        variables = ImmutableMap.of(
            "tags", foundTags,
            "sentiment", sentiment);
      } else {
        variables = ImmutableMap.of(
            "questions", new ArrayList<>(),
            "tags", new ArrayList<>(),
            "sentiment", -1);
      }

      /*-------*/
      /*
      You can get all the tags in the database using the jtDatabase getAllTagsFromDb method and
      find which ones apply using your vectorizor. Then you can find all questions that use those
      tags using findQuestionsFromTag (returns a List of Questions).
       */
      /*-------*/

      /*-------*/
      /*
      When a user selects a question, that question should be added to the entry string. This would
      essentially be updating the entry in the database, and would have similar functionality
      to HandleSaveUserEntry. I can also easily create an appendToCurrentEntry method to add to
      the entry. This would require the entry's id, the username, and the text to be appended.
       */
      /*-------*/

      return GSON.toJson(variables);
    }
  }

  public static class HandleCreateEntry implements Route {
    private static final Gson GSON = new Gson();

    //     Request ==>
//     Response ==> id of the entry that was created, store on the frontend and use it to request
//     specific entries from the backend
    @Override
    public Object handle(Request request, Response response) throws Exception {
      // If state requestQuestions Saves previous response list and new selected question, entry tags created here
      // If state start just gives questions

      JSONObject data = new JSONObject(request.body());
      Map<String, Object> variables;

      String userNameOrUserID = data.getString("userID");
      String state = data.getString("state");

      JournalTexterDB jtDB = new JournalTexterDB();
      int entryId = jtDB.addUserEntry(LocalDate.now(), "", userNameOrUserID);

      variables = ImmutableMap.of(
          "entryId", entryId
      );

      return GSON.toJson(variables);
    }
  }
//
//  public static class HandleClickSaveButton implements Route {
//    private static final Gson GSON = new Gson();
//    // Saves the last response list, entry tags created here
//    /**
//     * Handles Axios requests from the javascript front-end and returns
//     * the appropriate JSON object to be used by the front-end.
//     *
//     * @param request - request object for Axios request
//     * @param response - response object for Axios request
//     * @return a JSON object representing information to be used by the front end
//     * @throws Exception if data cannot be accessed from given JSON object
//     */
//    @Override
//    public Object handle(Request request, Response response) throws Exception {
//
//      JSONObject data = new JSONObject(request.body());
//      Map<String, Object> variables;
//
//      Integer entryId = Integer.parseInt(data.getString("entryID"));
//      String userNameOrUserID = data.getString("userID");
//      String text = data.getString("text");
//      // probably will not even need this, ideally from backend should be able to detect
//      // the most recently saved entry and get that from SQL using a query
//      WordCountVec vectorizor = new WordCountVec();
//      variables = vectorizor.parseToGui();
//      /*-------*/
//      /*
//      Currently there is only a method to create a new entry row in the entries table.
//      If we want to continually update a given entry, we would have to use its id to retrieve
//      the specific entry.
//
//      One way we can do this is by having separate handlers/methods/etc. for starting an entry
//      and saving in an entry. For instance, on creation of an entry we would query the database
//      to find the new entry's id and pass that to the frontend. Then, whenever we save the entry,
//      we can pass the entry id as well so that Java knows which entry to update.
//
//      Alternatively, if we ensure that each entry has a unique date, we could
//      request and update a given entry that way.
//       */
//      /*-------*/
//      return GSON.toJson(variables);
//    }
//  }

  public static class HandleRequestUserHistorySummary implements Route {
    private static final Gson GSON = new Gson();

    /**
     * Handles Axios requests from the javascript front-end and returns
     * the appropriate JSON object to be used by the front-end.
     *
     * @param request  - request object for Axios request
     * @param response - response object for Axios request
     * @return a JSON object representing information to be used by the front end
     * @throws Exception if data cannot be accessed from given JSON object
     */
    @Override
    public Object handle(Request request, Response response) throws Exception {

      JSONObject data = new JSONObject(request.body());
      Map<String, Object> variables;

      String userNameOrUserID = data.getString("token");

      List<HashMap<String, Object>> entriesMaps
          = BackendConnection.getEntriesSummaryFromUsername(userNameOrUserID);

      variables = ImmutableMap.of(
          "entries", entriesMaps);

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

  public static class HandleRequestUserSpecificHistory implements Route {
    private static final Gson GSON = new Gson();

    /**
     * Handles Axios requests from the javascript front-end and returns
     * the appropriate JSON object to be used by the front-end.
     *
     * @param request  - request object for Axios request
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

      JournalTexterDB jtDB = new JournalTexterDB();
      Entry<JournalText> entry = jtDB.getEntryById(entryId);

      List<String> questions = new ArrayList<>();
      List<List<String>> accumulatedResponses = new ArrayList<>();
      List<String> responsesForQuestion = new ArrayList<>();

      for (JournalText jt : entry.getQuestionsAndResponses()) {
        if (jt.getType() == JournalTextType.QUESTION) {
          questions.add(jt.getText());
          accumulatedResponses.add(responsesForQuestion);
          responsesForQuestion = new ArrayList<>();
        } else {
          responsesForQuestion.add(jt.getText());
        }
      }

      variables = ImmutableMap.of(
          "questions", questions,
          "responses", accumulatedResponses,
          "date", entry.getDate(),
          "tags", entry.getTags(),
          "sentiment", entry.getSentiment());

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
