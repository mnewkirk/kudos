package com.matthewnewkirk.kudos.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * This is taken from ixl.sql.util.DBUtils, but we want to decouple these projects.
 * @author mnewkirk
 *         created 7/30/2015
 */
public class DBUtil {
  /**
   * This is utilized by our containers and reporter to know what the proper SQL date format is.
   */
  public static final String DATESTAMP_FORMAT = "yyyy-MM-dd";
  public static final String TIMESTAMP_FORMAT = DATESTAMP_FORMAT + " HH:mm:ss";
  public static final long DAY_IN_MS = (1000 * 60 * 60 * 24);
  public static Date epochStartDate;
  public static Date epochEndDate;
  static {
    try {
      DBUtil.epochStartDate = DBUtil.parseStringToDateAndTime("1900-01-01 00:00:00");
      DBUtil.epochEndDate = DBUtil.parseStringToDateAndTime("3000-12-31 23:59:59");
    }
    catch (ParseException exception) {
      exception.printStackTrace();
    }
  }

    /**
   * This converts a Date object into our MySQL date format.
   * @return a String containing the proper date format
   */
  public static String formatDateAndTimeToString(Date timeWhenFoundDate) {
    DateFormat df = new SimpleDateFormat(TIMESTAMP_FORMAT);
    return df.format(timeWhenFoundDate);
  }

  /**
   * This parses string versions of the date+time into a date object.
   * It returns null if there is a parsing exception.
   */
  public static Date parseStringToDateAndTime(String stringDate) throws ParseException {
    DateFormat df = new SimpleDateFormat(TIMESTAMP_FORMAT);
    return df.parse(stringDate);
  }

  /**
   * A handy utility for finding today's date/time (at midnight).
   */
  public static Date todaysDate() {
    Calendar today = new GregorianCalendar();
    today.set(Calendar.HOUR, 0);
    today.set(Calendar.AM_PM, Calendar.AM);
    today.set(Calendar.MINUTE, 0);
    today.set(Calendar.SECOND, 0);
    today.set(Calendar.MILLISECOND, 0);
    return today.getTime();
  }

  /**
   * A handy utility for finding yesterday's date/time (at midnight).
   */
  public static Date yesterdaysDate() {
    return new Date(todaysDate().getTime() - DAY_IN_MS);
  }

  /**
   * A handy utility for finding yesterday's date/time (at midnight).
   */
  public static Date lastWeeksDate() {
    return new Date(todaysDate().getTime() - (7 * DAY_IN_MS));
  }
  /**
   * A simple parser for REST and CLI applications.
   */
  public static Date parseCommonDates(String dateString) throws ParseException {
    switch (dateString.toLowerCase()) {
      case "-1" :
      case "today" :
        return todaysDate();
      case "yesterday" :
        return yesterdaysDate();
      case "now" :
        return new Date();
      case "beginning" :
      case "start" :
        return epochStartDate;
      case "end" :
        return epochEndDate;
      case "lastWeek" :
        return lastWeeksDate();
      default :
        return parseStringToDateAndTime(dateString);
    }
  }
}
