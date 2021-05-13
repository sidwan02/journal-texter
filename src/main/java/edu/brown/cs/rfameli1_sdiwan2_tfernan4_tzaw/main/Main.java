package edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.main;


import java.io.PrintWriter;
import java.io.StringWriter;

import edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.JournalTexterDB.JournalTexterDB;
import edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.JournalTexterREPL.JournalTexterREPL;
import edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.REPL.REPL;
import edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.postRequestHandler.GUIHandler;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import spark.ExceptionHandler;
import spark.Request;
import spark.Response;
import spark.Spark;


/**
 * The Main class of our project. This is where execution begins.
 */
public final class Main {

  private static final int DEFAULT_PORT = 4567;

  /**
   * The initial method called when execution begins.
   *
   * @param args An array of command line arguments
   */
  public static void main(String[] args) {
    new Main(args).run();
  }

  private String[] args;

  private Main(String[] args) {
    this.args = args;
  }

//  https://sparktutorials.github.io/2015/08/24/spark-heroku.html
  static int getHerokuAssignedPort() {
    ProcessBuilder processBuilder = new ProcessBuilder();
    if (processBuilder.environment().get("PORT") != null) {
      return Integer.parseInt(processBuilder.environment().get("PORT"));
    }
    return DEFAULT_PORT; //return default port if heroku-port isn't set (i.e. on localhost)
  }

  private void run() {
    // Parse command line arguments
    OptionParser parser = new OptionParser();
    parser.accepts("gui");
//    parser.accepts("port").withRequiredArg().ofType(Integer.class)
//        .defaultsTo(DEFAULT_PORT);
    parser.accepts("port").withRequiredArg().ofType(Integer.class)
      .defaultsTo(getHerokuAssignedPort());
    OptionSet options = parser.parse(args);
    runSparkServer((int) options.valueOf("port"));

//    WordnikAPIHandler hoho = new WordnikAPIHandler();
//    hoho.getSynonyms("earth");
//
//    SentimentAnalysis pyt = new SentimentAnalysis();
//    System.out.println("oh yeah wooo: ");


//    SentimentAnalysis senti = new SentimentAnalysis();
//    Double sentiment = senti.getSentimentFromText("hello there");
//    System.out.println(sentiment);

    new JournalTexterREPL(new REPL(), JournalTexterDB.getInstance()).start();
  }

  private static void runSparkServer(int port) {
    Spark.port(port);
    Spark.externalStaticFileLocation("src/main/resources/static");
    Spark.exception(Exception.class, new ExceptionPrinter());

    Spark.options("/*", (request, response) -> {
      String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
      if (accessControlRequestHeaders != null) {
        response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
      }

      String accessControlRequestMethod = request.headers("Access-Control-Request-Method");

      if (accessControlRequestMethod != null) {
        response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
      }

      return "OK";
    });

    Spark.before((request, response) -> response.header("Access-Control-Allow-Origin", "*"));


    // Setup Spark Routes
    Spark.post("/login", new GUIHandler.HandleLogin());
    Spark.post("/signup", new GUIHandler.HandleSignUp());
    Spark.post("/handleRequestQuestion", new GUIHandler.HandleRequestQuestion());
    Spark.post("/handleSaveUserInputs", new GUIHandler.HandleSaveUserInputs());
    Spark.post("/handleCreateEntry", new GUIHandler.HandleCreateEntry());
    Spark.post("/handleUserHistorySummary", new GUIHandler.HandleRequestUserHistorySummary());
    Spark.post("/handleUserHistoryRequest", new GUIHandler.HandleRequestUserSpecificHistory());
    Spark.post("/handleDeletionRequest", new GUIHandler.HandleDeletionRequest());
  }

  /**
   * Display an error page when an exception occurs in the server.
   */
  private static class ExceptionPrinter implements ExceptionHandler {
    @Override
    public void handle(Exception e, Request req, Response res) {
      res.status(500);
      StringWriter stacktrace = new StringWriter();
      try (PrintWriter pw = new PrintWriter(stacktrace)) {
        pw.println("<pre>");
        e.printStackTrace(pw);
        pw.println("</pre>");
      }
      res.body(stacktrace.toString());
    }
  }
}
