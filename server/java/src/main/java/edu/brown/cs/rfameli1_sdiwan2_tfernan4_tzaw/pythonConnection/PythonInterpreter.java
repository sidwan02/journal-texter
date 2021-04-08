package edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.pythonConnection;//package edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.pythonConnection;
//// https://stackoverflow.com/questions/16460468/can-we-call-a-python-method-from-java
//    // https://stackoverflow.com/questions/9381906/how-to-call-a-python-method-from-a-java-class

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class PythonInterpreter {
  public String getStringOutput(String file) {
    try {
      // https://stackoverflow.com/questions/27267391/running-a-py-file-from-java
      String command = "python " + file;
      Process p = Runtime.getRuntime().exec(command);
//      Process p = Runtime.getRuntime().exec("python " + file);
      BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
      String ret = null;
      String finalOutput = null;
      while ((ret = in.readLine()) != null) {
        System.out.println(ret);
        finalOutput = ret;
      }

      return finalOutput.split(" ")[finalOutput.split(" ").length - 1];
//      System.out.println("ret);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return "";
  }
}