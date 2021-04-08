package edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.main;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.postRequestHandler.GUIHandler;
import edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.APIHandlers.wordSyonyms.WordnikAPIHandler;
import edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.sentimentAnalysis.SentimentAnalysis;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import spark.ExceptionHandler;
import spark.Request;
import spark.Response;
import spark.Spark;
import spark.template.freemarker.FreeMarkerEngine;

import freemarker.template.Configuration;


/**
 * The Main class of our project. This is where execution begins.
 *
 */
public final class Main {

  private static final int DEFAULT_PORT = 4567;

  /**
   * The initial method called when execution begins.
   *
   * @param args
   *          An array of command line arguments
   */
  public static void main(String[] args) {
    new Main(args).run();
  }

  private String[] args;

  private Main(String[] args) {
    this.args = args;
  }

  private void run() {
    // Parse command line arguments
    OptionParser parser = new OptionParser();
    parser.accepts("gui");
    parser.accepts("port").withRequiredArg().ofType(Integer.class)
      .defaultsTo(DEFAULT_PORT);
    OptionSet options = parser.parse(args);

    if (options.has("gui")) {
      runSparkServer((int) options.valueOf("port"));
    }

    WordnikAPIHandler hoho = new WordnikAPIHandler();
    hoho.getSynonyms("earth");

    SentimentAnalysis pyt = new SentimentAnalysis();
    String cool = pyt.getStringOutput("horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible ");
    System.out.println("oh yeah wooo: " + cool);
  }

  private static FreeMarkerEngine createEngine() {
    Configuration config = new Configuration();
    File templates = new File("src/main/resources/spark/template/freemarker");
    try {
      config.setDirectoryForTemplateLoading(templates);
    } catch (IOException ioe) {
      System.out.printf("ERROR: Unable use %s for template loading.%n",
        templates);
      System.exit(1);
    }
    return new FreeMarkerEngine(config);
  }

  private void runSparkServer(int port) {

    Spark.port(port);
    Spark.externalStaticFileLocation("src/main/resources/static");

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
    Spark.exception(Exception.class, new ExceptionPrinter());

    FreeMarkerEngine freeMarker = createEngine();

    // Setup Spark Routes
//    Spark.get("/stars", new StarsGuiHandler.FrontHandler(), freeMarker);
    // get user input
    Spark.post("/handleUserResponse", new GUIHandler.HandleRequestQuestion());
    Spark.post("/handleSaveUserEntry", new GUIHandler.HandleSaveUserEntry());
    Spark.post("/handleUserHistoryRequest", new GUIHandler.HandleUserHistoryRequest());
    Spark.post("/handleUserHistorySummary", new GUIHandler.HandleUserHistorySummary());
    Spark.post("/handleSelectedQuestion", new GUIHandler.HandleSelectedQuestion());
  }

  /**
   * Display an error page when an exception occurs in the server.
   *
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
