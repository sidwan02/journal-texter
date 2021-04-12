package edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.REPL;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.REPL.ArgTypes.DOUBLE;
import static edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.REPL.ArgTypes.STRING;
import static edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.REPL.ArgTypes.INT;


/**
 * Validates arguments passed into a REPL command.
 */
public final class ArgValidator {

  private ArgValidator() { }

  /**
   * Checks through every argFormat in argFormats to see if args is valid.
   *
   * @param command    The command on which the arguments will be called
   * @param argFormats A List of ArgTypes that specify what format the command's arguments must be
   *                   in
   * @param args       The arguments that will be checked
   * @return an ArgHolder with all the arguments in their proper type
   */
  public static ArgHolder parseInputByFormats(String command, List<List<ArgTypes>> argFormats,
                                              List<String> args)
      throws IllegalArgumentException {
    if (argFormats == null) {
      return null;
    }

    // Check through every argFormat to see if args matches the specified inputs
    ArgHolder argHolder;
    for (List<ArgTypes> argFormat : argFormats) {
      try {
        argHolder = parseInput(command, argFormat, args);
        return argHolder;
      } catch (IllegalArgumentException ignored) {
      }
    }
    // If no inputFormat was found to match the args, throw an exception
    throw new IllegalArgumentException(createArgTypeErrorString(command, argFormats));
  }

  /**
   * Checks if input arguments are valid according to the inputFormat.
   *
   * @param command     The command for which parseInput is validating arguments for
   * @param inputFormat A List of ArgTypes that specifies what format the args must be in.
   * @param args        a List<String> of arguments to be checked for validity
   * @return an ArgHolder with all the arguments in their proper type
   */
  private static ArgHolder parseInput(String command, List<ArgTypes> inputFormat, List<String> args)
      throws IllegalArgumentException {
    ArgHolder outArgHolder = new ArgHolder();

    if (inputFormat.size() != args.size()) {
      throw new IllegalArgumentException(createArgTypeErrorString(command,
          Collections.singletonList(inputFormat)));
    }

    // Check through every String in argFormatTypes and store the types in outArgHolder
    for (int i = 0; i < inputFormat.size(); i++) {
      switch (inputFormat.get(i)) {
        case STRING:
          outArgHolder.addString(args.get(i));
          break;
        case DOUBLE:
          outArgHolder.addDouble(Double.parseDouble(args.get(i)));
          break;
        case INT:
          outArgHolder.addInt(Integer.parseInt(args.get(i)));
          break;
        default:
          throw new IllegalArgumentException(createArgTypeErrorString(command,
              Collections.singletonList(inputFormat)));
      }
    }
    return outArgHolder;
  }

  /**
   * Prints an error that shows what arguments the command must take in.
   *
   * @param command    The command that is in use
   * @param argFormats A List of possible arrangements of ArgTypes that describe what the
   *                   command can take as arguments
   * @return a String that represents the required arguments for a command.
   */
  public static String createArgTypeErrorString(String command, List<List<ArgTypes>> argFormats) {
    StringBuilder outputErrorString = new StringBuilder(command + " requires arguments ");
    for (int i = 0; i < argFormats.size(); i++) {
      String stringRep = argFormatToString(argFormats.get(i));
      if (i == argFormats.size() - 1) {
        outputErrorString.append(stringRep);
      } else {
        outputErrorString.append(stringRep).append("or ");
      }
    }
    return "ERROR: " + outputErrorString;
  }


  /**
   * Changes an argFormat String into its String form.
   * @param argFormat the argument types to be parsed into strings
   * @return a String representation of the argFormat
   */
  public static String argFormatToString(List<ArgTypes> argFormat) {
    ArrayList<String> argStringsList = new ArrayList<String>();
    for (ArgTypes argType : argFormat) {
      switch (argType) {
        case STRING:
          argStringsList.add("String");
          break;
        case DOUBLE:
          argStringsList.add("double");
          break;
        case INT:
          argStringsList.add("integer");
          break;
        default:
          break;
      }
    }
    StringBuilder stringRep = new StringBuilder();
    for (String str : argStringsList) {
      stringRep.append(str).append(" ");
    }
    return stringRep.toString();
  }
}
