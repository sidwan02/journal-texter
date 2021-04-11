package edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.login;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.encryption.Encryptor;
import org.json.JSONObject;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.Map;

/**
 * Handles requests from the frontend to the user login server.  Takes in a request
 * with a username and a password and checks to see if a user exists.  If they do, it returns
 * the token, if not it returns an error.
 */
public class LoginHandler implements Route {
  private static final Gson GSON = new Gson();

  @Override
  public Object handle(Request request, Response response) throws Exception {
    JSONObject data = new JSONObject(request.body());
    String username = data.getString("username");
    String password = data.getString("password");

    //TODO connect username and password to database

    boolean validUserNamePassword = username.equals("theo");
    String failedLoginMessage = "Unable to login";

    String encodedToken = Encryptor.encodeMessage(username + password);

    if (validUserNamePassword) {
      Map<String, Object> variables = ImmutableMap.of("token", encodedToken);
      return GSON.toJson(variables);
    } else {
      response.status(401);
      return failedLoginMessage;
    }
  }
}