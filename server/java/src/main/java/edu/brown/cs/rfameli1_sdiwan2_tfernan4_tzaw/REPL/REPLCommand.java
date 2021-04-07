package edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.REPL;

import java.util.List;

public interface REPLCommand {
  String getCallWord();
  List<List<ArgTypes>> getArgFormats();
  void run(ArgHolder arguments);
}
