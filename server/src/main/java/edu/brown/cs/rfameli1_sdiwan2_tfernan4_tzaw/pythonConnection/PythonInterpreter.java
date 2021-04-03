package edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.pythonConnection;
// https://stackoverflow.com/questions/16460468/can-we-call-a-python-method-from-java
import org.python.core.PyInstance;


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

    ie.execfile("hello.py");

//    PyInstance hello = ie.createClass("Hello", "None");

//    int something = 10;

    ie.interpreter.set("something", 21);
//    PyObject pyResult = ie.interpreter.get("hello");

    // for invoke -> http://web.mit.edu/jython/jythonRelease_2_2alpha1/Doc/javadoc/org/python/core/PyInstance.html
//    hello.invoke("run");
    // https://stackoverflow.com/questions/9381906/how-to-call-a-python-method-from-a-java-class
//    ie.interpreter.eval("repr(Hello().run(something))");
    ie.interpreter.eval("repr(run(something))");
  }
}
