package edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.REPLTest;

import edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.REPL.ArgHolder;
import edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.REPL.ArgTypes;
import edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.REPL.ArgValidator;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.REPL.ArgTypes.*;
import static org.junit.Assert.*;

public class ArgValidatorTest {

  // ArgFormat strings
  List<ArgTypes> neighborsCoordinatesFormat = Arrays.asList(INT, DOUBLE, DOUBLE, DOUBLE);
  List<ArgTypes> neighborsNameFormat = Arrays.asList(INT, STRING);
  List<ArgTypes> radiusCoordinatesFormat = Arrays.asList(DOUBLE, DOUBLE, DOUBLE, DOUBLE);
  List<ArgTypes> allIncludedTypes = Arrays.asList(INT, STRING, DOUBLE);
  List<ArgTypes> radiusNameFormat = Arrays.asList(DOUBLE, STRING);

  // Arguments to check against ArgFormats
  List<String> neighborsCoordinatesArgs = Arrays.asList("5", "1.0", "2.3", "3");
  List<String> neighborsNameArgs = Arrays.asList("4", "something");
  List<String> invalidNegative = Arrays.asList("-5", "bad");
  List<String> invalidDouble = Arrays.asList("5.3", "stringy");

  ArgHolder ah;

  @Test
  public void testParseInputByFormats() {
    // neighborsCoordinatesArgs matches neighborsCoordinatesFormat, so the resulting ArgHolder
    // will hold an integer and three doubles
    ah = ArgValidator.parseInputByFormats("naive_neighbors",
        Arrays.asList(neighborsCoordinatesFormat, neighborsNameFormat, radiusCoordinatesFormat),
        neighborsCoordinatesArgs);
    assertNull(ah.nextString());
    assertEquals(Optional.ofNullable(ah.nextInt()), Optional.of(5));
    assertEquals(ah.getDoubles(), Arrays.asList(1.0, 2.3, 3.0));

    // valid arguments; ah will hold an integer and a string
    ah = ArgValidator.parseInputByFormats("naive_neighbors",
        Arrays.asList(neighborsCoordinatesFormat, neighborsNameFormat, radiusCoordinatesFormat),
        neighborsNameArgs);
    assertEquals(ah.nextString(), "something");
    assertEquals(Optional.ofNullable(ah.nextInt()), Optional.of(4));
  }

  @Test
  public void testCreateArgTypeErrorString() {
    assertEquals(
        ArgValidator.createArgTypeErrorString("naive_neighbors", new ArrayList<>()),
        "ERROR: naive_neighbors requires arguments ");
    assertEquals(
        ArgValidator.createArgTypeErrorString("naive_radius",
            Arrays.asList(neighborsCoordinatesFormat, neighborsNameFormat)),
        "ERROR: naive_radius requires arguments integer double double double or "
            + "integer String ");
  }

  @Test
  public void testArgFormatToString() {
    assertEquals(ArgValidator.argFormatToString(Arrays.asList(INT)), "integer ");
    assertEquals(ArgValidator.argFormatToString(Arrays.asList(DOUBLE, STRING, INT)),
        "double String integer ");
  }
}