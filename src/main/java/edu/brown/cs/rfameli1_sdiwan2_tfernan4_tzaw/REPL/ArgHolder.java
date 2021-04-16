package edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.REPL;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds arguments to be passed into a command.
 */
public class ArgHolder {
  private List<String> strings;
  private List<Double> doubles;
  private List<Integer> ints;

  /**
   * Creates a new ArgHolder with empty ArrayLists as fields.
   */
  public ArgHolder() {
    this.strings = new ArrayList<>();
    this.doubles = new ArrayList<>();
    this.ints = new ArrayList<>();
  }

  /**
   * Creates a new ArgHolder with initialized strings, doubles, and integers.
   * @param strings A list of strings
   * @param doubles A list of doubles
   * @param ints A list of integers
   */
  public ArgHolder(List<String> strings, List<Double> doubles, List<Integer> ints) {
    this.strings = new ArrayList<>(strings);
    this.doubles = new ArrayList<>(doubles);
    this.ints = new ArrayList<>(ints);
  }

  /**
   * Retrieves the integers from ints.
   * @return a List of integers
   */
  public List<Integer> getInts() {
    return new ArrayList<>(ints);
  }

  /**
   * Gets the next integer from the ArgHolder's integers, removing it from the list afterwards.
   * @return the next integer in the ints field, null if the ints field is empty
   */
  public Integer nextInt() {
    if (ints.size() > 0) {
      int outInt = ints.get(0);
      ints.remove(0);
      return outInt;
    } else {
      return null;
    }
  }

  /**
   * Adds an integer to the ArgHolder's ints list.
   * @param i the integer to be added
   */
  public void addInt(Integer i) {
    ints.add(i);
  }

  /**
   * Retrieves the doubles from the ArgHolder.
   * @return the list of doubles
   */
  public List<Double> getDoubles() {
    return new ArrayList<>(doubles);
  }

  /**
   * Gets the next double from the ArgHolder's doubles list, removing it from the list afterwards.
   * @return the next double in the doubles field, null if the doubles field is empty
   */
  public Double nextDouble() {
    if (doubles.size() > 0) {
      double outDouble = doubles.get(0);
      doubles.remove(0);
      return outDouble;
    } else {
      return null;
    }
  }

  /**
   * Adds a double to the ArgHolder's doubles list.
   * @param d the double to be added
   */
  public void addDouble(Double d) {
    doubles.add(d);
  }

  /**
   * Gets the next String from the ArgHolder's Strings list, removing it from the list afterwards.
   * @return the next String in the Strings field, null if the Strings field is empty
   */
  public String nextString() {
    if (strings.size() > 0) {
      String outString = strings.get(0);
      strings.remove(0);
      return outString;
    } else {
      return null;
    }
  }

  /**
   * Adds a String to the ArgHolder's Strings list.
   * @param s the String to be added
   */
  public void addString(String s) {
    strings.add(s);
  }
}
