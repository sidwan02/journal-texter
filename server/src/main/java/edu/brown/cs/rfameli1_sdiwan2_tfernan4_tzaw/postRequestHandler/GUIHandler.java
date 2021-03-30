package edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.postRequestHandler;

import com.google.gson.Gson;
import edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.wordCountVec.WordCountVec;
import edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.wordnikAPIHandler.WordnikAPIHandler;
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

  public static class TagsHandler implements Route {
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

      String textIdentifier = data.getString("textIdentifier");
      // probably will not even need this, ideally from backend should be able to detect
      // the most recently saved entry and get that from SQL using a query

      WordCountVec vectorizor = new WordCountVec();
      variables = vectorizor.parseToGui();

      return GSON.toJson(variables);
    }
  }
}
