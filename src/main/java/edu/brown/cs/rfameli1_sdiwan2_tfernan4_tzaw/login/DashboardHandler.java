package edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.login;

import com.google.common.collect.ImmutableMap;
import org.json.JSONObject;
import spark.Request;
import spark.Response;
import spark.Route;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//TODO DELETE THIS FILE

public class DashboardHandler implements Route {
  private static final Gson GSON = new Gson();

  @Override
  public Object handle(Request request, Response response) throws Exception {
    JSONObject data = new JSONObject(request.body());
    String token = data.getString("token");

    List<String> entries = new ArrayList<>();
    entries.add("Date1");
    entries.add("Date2");
    entries.add("Date3");

    boolean validUserName = token.equals("theo");

    if (validUserName) {
      Map<String, Object> variables = ImmutableMap.of("entries", entries);
      return GSON.toJson(variables);
    } else {
      response.status(500);
      return "Could not find data";
    }
  }
}
