package edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.sentimentAnalysis;
// https://stackoverflow.com/questions/16460468/can-we-call-a-python-method-from-java
// https://stackoverflow.com/questions/9381906/how-to-call-a-python-method-from-a-java-class

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class SentimentAnalysis {
  private Process mProcess;

  public double getSentimentFromText(String review) {
    // https://stackoverflow.com/questions/27267391/running-a-py-file-from-java
    // https://www.edureka.co/community/358/how-to-execute-a-python-file-with-few-arguments-in-java
    Process process;
    try {
      process = Runtime.getRuntime().exec(new String[] {
          "python.exe",
          "server/python/script_python.py",
          review});
      mProcess = process;
    } catch (Exception e) {
      System.out.println("Exception Raised" + e.toString());
    }
    InputStream stdout = mProcess.getInputStream();
    BufferedReader reader =
        new BufferedReader(new InputStreamReader(stdout, StandardCharsets.UTF_8));
    String line;
    String finalOutput = null;
    try {
      while ((line = reader.readLine()) != null) {
        finalOutput = line;
      }
    } catch (IOException e) {
      System.out.println("Exception in reading output" + e.toString());
    }
    return Double.parseDouble(
      finalOutput.split(" ")[finalOutput.split(" ").length - 1]);
  }
}
