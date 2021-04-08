package edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.pythonConnection;//package edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.pythonConnection;
//// https://stackoverflow.com/questions/16460468/can-we-call-a-python-method-from-java
//    // https://stackoverflow.com/questions/9381906/how-to-call-a-python-method-from-a-java-class

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class PythonInterpreter {
  public static void main(String[] args) {
//    try(PythonInterpreter pyInterp = new PythonInterpreter()) {
//      pyInterp.exec("print('Hello Python World!')");
//    }
    try {
      Process p = Runtime.getRuntime().exec("python server/python/predict.py");
      BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
      String ret = null;
      while ((ret = in.readLine()) != null) {
        System.out.println(ret);
      }
//      System.out.println("ret);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}