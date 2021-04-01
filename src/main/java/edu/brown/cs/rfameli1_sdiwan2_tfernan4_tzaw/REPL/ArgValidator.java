package edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.REPL;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Validates arguments passed into a command.
 */
public final class ArgValidator {

  private ArgValidator() { }

  /**
   * Checks through every argFormat in argFormats to see if args is valid.
   *
   * @param command    The command on which the arguments will be called
   * @param argFormats A List of Strings that specify what format the args must be in. Each string
   *                   should be separated by spaces e.g. "s d i i i"
   *                   s - string
   *                   d - double
   *                   i - int
   *                   nni - non-negative int
   *                   nnd - non-negative double
   * @param args       The arguments that will be checked
   * @return an ArgHolder with all the arguments in their proper type
   */
  public static ArgHolder parseInputByFormats(String command, List<String> argFormats,
                                              List<String> args)
      throws IllegalArgumentException {
    if (argFormats == null) {
      return null;
    }

    // Check through every argFormat to see if args matches the specified inputs
    ArgHolder argHolder;
    for (String argFormat : argFormats) {
      try {
        argHolder = parseInput(command, argFormat, args);
        if (argHolder != null) {
          return argHolder;
        }
      } catch (IllegalArgumentException e) {
      }
    }
    // If no inputFormat was found to match the args, throw an exception
    throw new IllegalArgumentException(createArgTypeErrorString(command, argFormats));
  }

  /**
   * Checks if input arguments are valid according to the inputFormat.
   *
   * @param command     The command for which parseInput is validating arguments for
   * @param inputFormat A string that specifies what format the args must be in. Should be
   *                    separated by spaces e.g. "s d i i i"
   *                    s - string
   *                    d - double
   *                    i - int
   *                    nni - non-negative int
   *                    nnd - non-negative double
   * @param args        a List<String> of arguments to be checked for validity
   * @return an ArgHolder with all the arguments in their proper type
   */
  private static ArgHolder parseInput(String command, String inputFormat, List<String> args)
      throws IllegalArgumentException {
    ArgHolder outArgHolder = new ArgHolder();
    List<String> argFormatTypes = new ArrayList<>(Arrays.asList(inputFormat.split(" ")));

    if (argFormatTypes.size() != args.size()) {
      throw new IllegalArgumentException(createArgTypeErrorString(command,
          new ArrayList<>(Arrays.asList(new String[] {inputFormat}))));
    }

    // Check through every String in argFormatTypes and store the types in outArgHolder
    for (int i = 0; i < argFormatTypes.size(); i++) {
      switch (argFormatTypes.get(i)) {
        case ("s"):
          outArgHolder.addString(args.get(i));
          break;
        case ("d"):
          outArgHolder.addDouble(Double.parseDouble(args.get(i)));
          break;
        case ("i"):
          outArgHolder.addInt(Integer.parseInt(args.get(i)));
          break;
        case ("nni"):
          int nonNegInt = Integer.parseInt(args.get(i));
          // Throw an Exception with an argTypeErrorString if negative
          if (nonNegInt < 0) {
            throw new IllegalArgumentException(createArgTypeErrorString(command,
                Arrays.asList(inputFormat)));
          } else {
            outArgHolder.addInt(Integer.parseInt(args.get(i)));
            break;
          }
        case ("nnd"):
          double nonNegDouble = Double.parseDouble(args.get(i));
          // Throw an Exception with an argTypeErrorString if negative
          if (nonNegDouble < 0) {
            throw new IllegalArgumentException(createArgTypeErrorString(command,
                Arrays.asList(inputFormat)));
          } else {
            outArgHolder.addDouble(Double.parseDouble(args.get(i)));
            break;
          }
        default:
          break;
      }
    }
    return outArgHolder;
  }

  /**
   * Prints an error that shows what arguments the command must take in.
   *
   * @param command    The command that is in use
   * @param argFormats A string of space-separated characters that describe what types each argument
   *                   should have
   * @return a String that represents the required arguments for a command.
   */
  public static String createArgTypeErrorString(String command, List<String> argFormats) {
    String outputErrorString = String.format(command + " requires arguments ");
    for (int i = 0; i < argFormats.size(); i++) {
      String stringRep = argFormatToString(argFormats.get(i));
      if (i == argFormats.size() - 1) {
        outputErrorString = String.format(outputErrorString + stringRep);
      } else {
        outputErrorString = String.format(outputErrorString + stringRep + "or ");
      }
    }
    return String.format("ERROR: " + outputErrorString);
  }


  /**
   * Changes an argFormat String into it's written out form
   * e.g. "s i i d nni" becomes "String, int, int, double, non-negative int"
   *
   * @param argFormat A string of space-separated characters that describe what types each argument
   *                  should have
   * @return a String representation of the argFormat
   */
  public static String argFormatToString(String argFormat) {
    ArrayList<String> argFormatList = new ArrayList<String>(Arrays.asList(argFormat.split(" ")));
    ArrayList<String> argStringsList = new ArrayList<String>();
    for (String argString : argFormatList) {
      switch (argString) {
        case ("s"):
          argStringsList.add("String");
          break;
        case ("d"):
          argStringsList.add("double");
          break;
        case ("i"):
          argStringsList.add("integer");
          break;
        case ("nni"):
          argStringsList.add("nonnegative-integer");
          break;
        case ("nnd"):
          argStringsList.add("nonnegative-double");
        default:
          break;
      }
    }
    String stringRep = "";
    for (String str : argStringsList) {
      stringRep = String.format(stringRep + str + " ");
    }
    return stringRep;
  }
}
