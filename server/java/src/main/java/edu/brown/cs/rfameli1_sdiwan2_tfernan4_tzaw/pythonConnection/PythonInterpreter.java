package edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.pythonConnection;
// https://stackoverflow.com/questions/16460468/can-we-call-a-python-method-from-java
import org.python.core.PyInstance;

import java.util.HashMap;


public class PythonInterpreter
{

  public org.python.util.PythonInterpreter interpreter;

  public PythonInterpreter()
  {
    org.python.util.PythonInterpreter.initialize(System.getProperties(),
      System.getProperties(), new String[0]);

    this.interpreter = new org.python.util.PythonInterpreter();
  }

  void execfile( final String fileName )
  {
    this.interpreter.execfile(fileName);
  }

  PyInstance createClass( final String className, final String opts )
  {
    return (PyInstance) this.interpreter.eval(className + "(" + opts + ")");
  }

  public static void main(String[] args)
  {
    PythonInterpreter ie = new PythonInterpreter();

    System.out.println("hi");

//    PyInstance hello = ie.createClass("Hello", "None");

//    int something = 10;

    HashMap<Integer, String> map = new HashMap<>();
    map.put(0, "hi");

//    ie.interpreter.exec("import en_core_web_sm");
//    ie.interpreter.exec("import numpy as np");
//    ie.interpreter.exec("import spacy");
//    ie.interpreter.exec("import itertools");
//    ie.interpreter.exec("import emoji");
//    ie.interpreter.exec("from vaderSentiment.vaderSentiment import SentimentIntensityAnalyzer");
//    ie.interpreter.exec("from nltk.tokenize import word_tokenize");
//    ie.interpreter.exec("from nltk.corpus import stopwords");
//    ie.interpreter.exec("import string");
//    ie.interpreter.exec("import re");

    ie.execfile("server/python/hello.py");

    ie.interpreter.set("something", map);
//    PyObject pyResult = ie.interpreter.get("hello");

    // for invoke -> http://web.mit.edu/jython/jythonRelease_2_2alpha1/Doc/javadoc/org/python/core/PyInstance.html
//    hello.invoke("run");
    // https://stackoverflow.com/questions/9381906/how-to-call-a-python-method-from-a-java-class
//    ie.interpreter.eval("repr(Hello().run(something))");
    ie.interpreter.eval("repr(run2(something))");
  }
}
