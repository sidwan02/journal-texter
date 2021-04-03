package edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.wordCountVec;
// https://stackoverflow.com/questions/16460468/can-we-call-a-python-method-from-java
import org.python.core.PyInstance;
import org.python.core.PyObject;
import org.python.core.PyType;
import org.python.util.PythonInterpreter;

import static org.python.core.Py.java2py;


public class InterpreterExample
{

  public PythonInterpreter interpreter = null;


  public InterpreterExample()
  {
    PythonInterpreter.initialize(System.getProperties(),
      System.getProperties(), new String[0]);

    this.interpreter = new PythonInterpreter();
  }

  void execfile( final String fileName )
  {
    this.interpreter.execfile(fileName);
  }

  PyInstance createClass( final String className, final String opts )
  {
    return (PyInstance) this.interpreter.eval(className + "(" + opts + ")");
  }

  public static void main( String gargs[] )
  {
    InterpreterExample ie = new InterpreterExample();

    ie.execfile("hello.py");

//    PyInstance hello = ie.createClass("Hello", "None");

//    int something = 10;

    ie.interpreter.set("something", 21);
//    PyObject pyResult = ie.interpreter.get("hello");

    // http://web.mit.edu/jython/jythonRelease_2_2alpha1/Doc/javadoc/org/python/core/PyInstance.html
//    hello.invoke("run");
    ie.interpreter.eval("repr(Hello().run(something))");
  }
}
