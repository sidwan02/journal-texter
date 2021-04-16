package edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.postRequestHandler;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.Journal.Entry;
import edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.Journal.JournalText;
import edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.Journal.JournalTextType;
import edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.Journal.Question;
import edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.JournalTexterDB;
import edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.encryption.Encryptor;
import org.json.JSONArray;
import org.json.JSONObject;
import spark.Request;
import spark.Response;
import spark.Route;

import javax.security.auth.login.FailedLoginException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * postRequestHandler class to manage all handlers to the JournalTexter Page.
 */
public class GUIHandler {

  /**
   * Class that manages all requests for new questions.
   */
  public static class HandleRequestQuestion implements Route {
    private static final Gson GSON = new Gson();

    /**
     * Handles request to send a new batch of questions to the frontend.
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

      // id of the entry
      Integer entryId = Integer.parseInt(data.getString("entryID"));
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

        JournalTexterDB jtDB = JournalTexterDB.getInstance();

        // save the question
        jtDB.addToEntry(entryId, entryInfo);

        variables = ImmutableMap.of(
            "questions", questions,
            "tags", new ArrayList<>(), // no responses data
            "sentiment", -1); // no responses data
      } else if (state.equals("requestQuestion")) {
        // convert the JSONArray
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

        // in case not enough questions are determined from tags
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
        // should not be entering this state
        variables = ImmutableMap.of(
            "questions", new ArrayList<>(),
            "tags", new ArrayList<>(),
            "sentiment", -1);
      }

      return GSON.toJson(variables);
    }
  }

  /**
   * Class that manages all requests to save user inputs.
   */
  public static class HandleSaveUserInputs implements Route {
    private static final Gson GSON = new Gson();

    /**
     * Handles request to save data either from clicking on a new question or saving the entry.
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

      // id of the entry
      Integer entryId = Integer.parseInt(data.getString("entryID"));
      // question selected by user
      String question = data.getString("question");
      // list of responses in the form of a JSON array
      JSONArray text = data.getJSONArray("text");
      // either "saveQuestion" or "saveEntry"
      // "saveQuestion" -> after the user clicks on a new question
      // "saveEntry" -> after the user clicks on the save btn
      String state = data.getString("state");

      if (state.equals("saveQuestion")) {
        // convert the JSONArray
        List<String> responses = new ArrayList<>();
        if (text != null) {
          int len = text.length();
          for (int i = 0; i < len; i++) {
            responses.add(text.get(i).toString());
          }
        }

        String combinedResponses = String.join(" ", responses);

        Set<String> foundTags = BackendConnection.getTagsFromResponses(combinedResponses);

        double sentiment = -1.0;
        //double sentiment = BackendConnection.getSentimentFromResponses(combinedResponses);

        // save the responses
        List<JournalText> entryInfo = new ArrayList<>();
        for (String res : responses) {
          entryInfo.add(new edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.Journal.Response(res));
        }

        // save the question
        entryInfo.add(new Question(question));

        JournalTexterDB jtDB = JournalTexterDB.getInstance();

        jtDB.addToEntry(entryId, entryInfo);

        variables = ImmutableMap.of(
          "tags", foundTags,
          "sentiment", sentiment);
      } else if (state.equals("saveEntry")) {
        // convert JSONArray
        List<String> responses = new ArrayList<>();
        if (text != null) {
          int len = text.length();
          for (int i = 0; i < len; i++) {
            responses.add(text.get(i).toString());
          }
        }

        String combinedResponses = String.join(" ", responses);

        Set<String> foundTags = BackendConnection.getTagsFromResponses(combinedResponses);

        double sentiment = -1.0;
        //double sentiment = BackendConnection.getSentimentFromResponses(combinedResponses);

        // save the responses
        List<JournalText> entryInfo = new ArrayList<>();
        for (String res : responses) {
          entryInfo.add(new edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.Journal.Response(res));
        }

        JournalTexterDB jtDB = JournalTexterDB.getInstance();

        jtDB.addToEntry(entryId, entryInfo);

        variables = ImmutableMap.of(
            "tags", foundTags,
            "sentiment", sentiment);
      } else {
        // should not reach this branch
        variables = ImmutableMap.of(
            "questions", new ArrayList<>(),
            "tags", new ArrayList<>(),
            "sentiment", -1);
      }

      return GSON.toJson(variables);
    }
  }

  /**
   * Class that manages all requests to create a new entry.
   */
  public static class HandleCreateEntry implements Route {
    private static final Gson GSON = new Gson();

    /**
     * Handles request to create a new entry.
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

      String username = data.getString("userID");

      JournalTexterDB jtDB = JournalTexterDB.getInstance();

      // create a blank entry
      int entryId = jtDB.addUserEntry(LocalDate.now(), "", username);

      // give the entry ID for saving capabilities on this entry
      variables = ImmutableMap.of(
          "entryId", entryId
      );

      return GSON.toJson(variables);
    }
  }

  /**
   * Class that manages all requests for a user's history summary.
   */
  public static class HandleRequestUserHistorySummary implements Route {
    private static final Gson GSON = new Gson();

    /**
     * Handle request for a user's entry history.
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

      String username = data.getString("token");

      JSONArray entriesMaps
          = BackendConnection.getEntriesSummaryFromUsername(username);

      variables = ImmutableMap.of(
          "entries", entriesMaps);

      return GSON.toJson(variables);
    }
  }

  /**
   * Class that manages all requests for a specific entry's contents.
   */
  public static class HandleRequestUserSpecificHistory implements Route {
    private static final Gson GSON = new Gson();

    /**
     * Handles getting all data from a specific user entry.
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

      JournalTexterDB jtDB = JournalTexterDB.getInstance();

      Entry<JournalText> entry = jtDB.getEntryById(entryId);

      List<String> questions = new ArrayList<>();
      //List<List<String>> accumulatedResponses = new ArrayList<>();
      //List<String> responsesForQuestion = new ArrayList<>();
      List<String> responses = new ArrayList<>();

      List<JournalText> allText = entry.getQuestionsAndResponses();

      for (JournalText jt : allText) {
        // Separate out questions and responses
        if (jt.getType() == JournalTextType.QUESTION) {
          questions.add(jt.getText());
          // accumulatedResponses.add(responsesForQuestion);
          // responsesForQuestion = new ArrayList<>();
        } else {
          //responsesForQuestion.add(jt.getText());
          responses.add(jt.getText());
        }
      }

      variables = ImmutableMap.of(
          "questions", questions,
          //"responses", accumulatedResponses,
          "responses", responses,
          "date", entry.getDate(),
          //"tags", entry.getTags(),
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

  /**
   * Handles requests from the frontend to the user login server.  Takes in a request
   * with a username and a password and checks to see if a user exists.  If they do, it returns
   * the token, if not it returns an error.
   */
  public static class HandleSignUp implements Route {
    private static final Gson GSON = new Gson();

    /**
     * TODO
     *
     * @param request  - request object for Axios request
     * @param response - response object for Axios request
     * @return a JSON object representing information to be used by the front end
     * @throws Exception if data cannot be accessed from given JSON object
     */
    @Override
    public Object handle(Request request, Response response) throws Exception {
      JSONObject data = new JSONObject(request.body());
      String username = data.getString("username");
      String password = data.getString("password");

      byte[] encryptedPassword = Encryptor.encryptMessage(password);
      JournalTexterDB database = JournalTexterDB.getInstance();

      try {
        database.registerUser(username, encryptedPassword);

        Map<String, Object> variables = ImmutableMap.of("token", username);
        return GSON.toJson(variables);
      } catch (FailedLoginException flEx) {
        response.status(401);
        return flEx.getMessage();
      } catch (Exception e) {
        e.printStackTrace();
        response.status(500);
        return e.getMessage();
      }
    }
  }

  /**
   * Handles requests from the frontend to the user login server.  Takes in a request
   * with a username and a password and checks to see if a user exists.  If they do, it returns
   * the token, if not it returns an error.
   */
  public static class HandleLogin implements Route {
    private static final Gson GSON = new Gson();

    /**
     * TODO
     *
     * @param request  - request object for Axios request
     * @param response - response object for Axios request
     * @return a JSON object representing information to be used by the front end
     * @throws Exception if data cannot be accessed from given JSON object
     */
    @Override
    public Object handle(Request request, Response response) throws Exception {
      JSONObject data = new JSONObject(request.body());
      String username = data.getString("username");
      String password = data.getString("password");

      byte[] encryptedPassword = Encryptor.encryptMessage(password);
      JournalTexterDB database = JournalTexterDB.getInstance();

      try {
        database.authenticateUser(username, encryptedPassword);

        Map<String, Object> variables = ImmutableMap.of("token", username);
        return GSON.toJson(variables);
      } catch (FailedLoginException flEx) {
        response.status(401);
        return flEx.getMessage();
      } catch (Exception e) {
        e.printStackTrace();
        response.status(500);
        return e.getMessage();
      }
    }
  }
}
