package com.matthewnewkirk.kudos.forms;

import java.util.List;

import org.springframework.validation.ObjectError;

/**
 * @author Matt Newkirk 3/3/2016
 */
public class ErrorHelper {
  /**
   * Collate our errors into a string that can be output.
   */
  public static String collateErrors(List<ObjectError> allErrors) {
    StringBuilder stringBuilder = new StringBuilder();
    for (ObjectError error : allErrors) {
      stringBuilder.append(error.toString()).append("\n");
    }
    return stringBuilder.toString();
  }
}
