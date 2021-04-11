package edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.sentimentAnalysis;//package edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.sentimentAnalysis;
//// https://stackoverflow.com/questions/16460468/can-we-call-a-python-method-from-java
//    // https://stackoverflow.com/questions/9381906/how-to-call-a-python-method-from-a-java-class

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class SentimentAnalysis {
  Process mProcess;

  public double getSentimentFromText(String review) {
//    try {
//      // https://stackoverflow.com/questions/27267391/running-a-py-file-from-java
//      String command = "python " + file;
//      Process p = Runtime.getRuntime().exec(command + " " + "hello");
////      Process p = Runtime.getRuntime().exec("python " + file);
//      BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
//      String ret = null;
//      String finalOutput = null;
//      while ((ret = in.readLine()) != null) {
//        System.out.println(ret);
//        finalOutput = ret;
//      }
//
//      return finalOutput.split(" ")[finalOutput.split(" ").length - 1];
////      System.out.println("ret);
//    } catch (IOException e) {
//      e.printStackTrace();
//    }
//    return "";
//  }
    // https://www.edureka.co/community/358/how-to-execute-a-python-file-with-few-arguments-in-java
    Process process;
    try {
      process = Runtime.getRuntime().exec(new String[]{
        "python.exe",
        "server/python/script_python.py",
        review});
      mProcess = process;
    } catch (Exception e) {
      System.out.println("Exception Raised" + e.toString());
    }
    InputStream stdout = mProcess.getInputStream();
    BufferedReader reader = new BufferedReader(new InputStreamReader(stdout, StandardCharsets.UTF_8));
    String line;
    String finalOutput = null;
    try {
      while ((line = reader.readLine()) != null) {
//        System.out.println("stdout: " + line);
        finalOutput = line;
      }
    } catch (IOException e) {
      System.out.println("Exception in reading output" + e.toString());
    }
    return Double.parseDouble(finalOutput.split(" ")[finalOutput.split(" ").length - 1]);
  }
}