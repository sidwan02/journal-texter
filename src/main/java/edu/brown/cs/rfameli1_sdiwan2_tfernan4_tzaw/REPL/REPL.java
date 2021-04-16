package edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.REPL;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A Read-eval-print-loop.
 */
public class REPL {
  private final Pattern regexParse = Pattern.compile("(\".*?\"|\\S+)");
  private final HashSet<REPLCommand> registeredCommands;


  /**
   * Constructor that sets the set of registered commands to be empty.
   */
  public REPL() {
    this.registeredCommands = new HashSet<>();
  }

  /**
   * Registers a command class to the REPL.
   * @param replCommand a command class that implements REPLCommand
   */
  public void registerCommand(REPLCommand replCommand) {
    registeredCommands.add(replCommand);
  }

  /**
   * Starts the REPL.
   * @throws IOException if an error occurs with input or output
   */
  public void start() throws IOException {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    String input = br.readLine();
    String command;
    List<String> args;

    while (input != null) {
      Matcher inputMatcher = regexParse.matcher(input);
      List<String> parsedUserInput = new ArrayList<>();
      while (inputMatcher.find()) {
        parsedUserInput.add(inputMatcher.group());
      }
      if (parsedUserInput.size() != 0) {
        command = parsedUserInput.get(0);
        parsedUserInput.remove(0);
        args = parsedUserInput;
        ArgHolder argHolder;
        boolean foundCommand = false;
        // Run through every command and check if the input is valid for each
        for (REPLCommand aCommand : registeredCommands) {
          if (aCommand.getCallWord().equals(command)) {
            // Use the ArgValidator to ensure that the arguments matches one of the commands
            // possible argument formats (e.g. string, string, double)
            foundCommand = true;
            try {
              argHolder = ArgValidator.parseInputByFormats(command, aCommand.getArgFormats(), args);
              aCommand.run(argHolder);
            } catch (IllegalArgumentException e) {
              System.out.println(e.getMessage());
            }
          }
        }
        if (!foundCommand) {
          System.out.println("ERROR: Command " + command + " not found");
        }

      } else {
        System.out.println("ERROR: Invalid command entered");
      }
      // loop
      input = br.readLine();
    }
    br.close();
  }
}


