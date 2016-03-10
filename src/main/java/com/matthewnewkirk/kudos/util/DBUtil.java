package com.matthewnewkirk.kudos.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author mnewkirk
 *         created 7/30/2015
 */
public class DBUtil {
  /**
   * This is utilized by our containers and reporter to know what the proper SQL date format is.
   */
  public static final String TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss";
    /**
   * This converts a Date object into our MySQL date format.
   * @return a String containing the proper date format
   */
  public static String formatDateAndTimeToString(Date timeWhenFoundDate) {
    DateFormat df = new SimpleDateFormat(TIMESTAMP_FORMAT);
    return df.format(timeWhenFoundDate);
  }
  private DBUtil() {
    // Intentionally empty.
  }
}
