package edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.main;


import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.EnumSet;
import java.util.List;

import edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.APIHandlers.WordnikAPIHandler;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import net.jeremybrooks.knicker.AccountApi;
import net.jeremybrooks.knicker.Knicker;
import net.jeremybrooks.knicker.KnickerException;
import net.jeremybrooks.knicker.WordApi;
import net.jeremybrooks.knicker.dto.Related;
import net.jeremybrooks.knicker.dto.TokenStatus;
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

//    // Wordnik API connection
//    System.setProperty("WORDNIK_API_KEY", "70538348db6b42e43a5181e32070feebc0b303e293ed13a97");
    WordnikAPIHandler blooh = new WordnikAPIHandler();
    blooh.getSynonyms("earth");
//
//    try {
//      TokenStatus status = AccountApi.apiTokenStatus();
//      if (status.isValid()) {
//        System.out.println("API key is valid.");
//      } else {
//        System.out.println("API key is invalid!");
//        System.exit(1);
//      }
//    } catch (KnickerException e) {
//      System.out.println("shit");
//      e.printStackTrace();
//    }

//    try {
//      List<Related> list= WordApi.related(
//        "earth",
//        false,
//        EnumSet.of(Knicker.RelationshipType.synonym),
//        100);
//      for(Related synonymCollection : list){
//        for(String synonym : synonymCollection.getWords())
//          System.out.println(synonym);
//      }
//    }
//    catch (KnickerException e) {
//      e.printStackTrace();
//    }

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

//  private void runSparkServer(int port) {
//    Spark.port(port);
//    Spark.externalStaticFileLocation("src/main/resources/static");
//    Spark.exception(Exception.class, new ExceptionPrinter());
//
//    FreeMarkerEngine freeMarker = createEngine();
//
//    // Setup Spark Routes
//    Spark.get("/stars", new StarsGuiHandler.FrontHandler(), freeMarker);
//    // get user input
//    Spark.post("/csvLoaded", new StarsGuiHandler.SubmitHandlerCsv(), freeMarker);
//    Spark.post("/results", new StarsGuiHandler.SubmitHandlerCommand(), freeMarker);
//    Spark.post("/stars", new StarsGuiHandler.SubmitHandlerStars(), freeMarker);
//  }

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
//    Spark.post("/csvLoaded", new StarsGuiHandler.SubmitHandlerCsv(), freeMarker);
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
