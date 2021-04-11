package edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.postRequestHandler;

import com.google.gson.Gson;
import edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.JournalTexterDB;
import edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.wordCountVec.WordCountVec;
import org.json.JSONObject;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.Map;

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
      String text = data.getString("text");
      String startState = data.getString("start"); // "true" -> on load / ""
      // probably will not even need this, ideally from backend should be able to detect
      // the most recently saved entry and get that from SQL using a query
      WordCountVec vectorizor = new WordCountVec();
      variables = vectorizor.parseToGui();

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

  public static class HandleSelectedQuestion implements Route {
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


      WordCountVec vectorizor = new WordCountVec();
      variables = vectorizor.parseToGui();

      return GSON.toJson(variables);
    }
  }

  public static class HandleSaveUserEntry implements Route {
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
