package edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.utils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 * Utility class for converting between dates.
 */
public final class DateConversion {
  /**
   * Private constructor (utility class).
   */
  private DateConversion() { }

  /**
   * Converts a Date to a LocalDate.
   * Retrieved from https://www.javadevjournal.com/java/convert-date-to-localdate/
   * @param date a Date object
   * @return a LocalDate version of the input date
   */
  public static LocalDate dateToLocalDate(Date date) {
    return Instant.ofEpochMilli(date.getTime())
        .atZone(ZoneId.systemDefault())
        .toLocalDate();
  }
}
