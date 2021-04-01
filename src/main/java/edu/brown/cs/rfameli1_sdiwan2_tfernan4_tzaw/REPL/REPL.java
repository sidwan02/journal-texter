package edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.REPL;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
//
//public class REPL {
//  private String input = null;
//  private String command = null;
//  private String argString = null;
//  private List<String> args = null;
//  private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//  private final Pattern regexParse = Pattern.compile("(\".*?\"|\\S+)");
//
//  public REPL() {
//  }
//
//  public boolean readInput() {
//    try {
//      this.input = br.readLine();
//      if (input == null)
//        return false;
//
//      // Separate arguments enclosed by quotation marks before splitting by spaces
//      List<String> splitWords = new ArrayList<(Arrays.asList(input.split("\"")));
//      if (splitWords.size() == 1)
//        args = new ArrayList<>(Arrays.asList(splitWords.get(0).split(" ")));
//      else if (splitWords.size() == 2) {
//        args = new ArrayList<>(Arrays.asList(splitWords.get(0).split(" ")));
//        args.add(splitWords.get(1));
//      } else {
//        // BUG using quotation marks in a way not specified as a command will cause an error
//        args = new ArrayList(Arrays.asList(splitWords.get(0).split(" ")));
//        System.out.println("ERROR: Invalid input, try using fewer quotation marks");
//      }
//
//      command = args.get(0);
//      args.remove(0);
//      return true;
//    } catch (Exception e) {
//      System.out.println("ERROR: Invalid input to REPL");
//      return false;
//    }
//  }
//
//  public String getCommand() {
//    return command;
//  }
//
//  public List<String> getArgs() {
//    return args;
//  }
//}


