package com.matthewnewkirk.kudos.forms;

import com.matthewnewkirk.kudos.util.UserUtil;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

/**
 * @author Matt Newkirk 1/21/2016
 */
public class LoginForm {
  public static String defaultUsername = "<username>";
  public static String defaultPassword = "<password>";
  String username;
  String password;
  String feedback;

  public LoginForm(String username, String password) {
    this.username = username;
    this.password = password;
    this.feedback = "";
  }

  public LoginForm() {
    username = defaultUsername;
    password = defaultPassword;
    feedback = "";
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getFeedback() {
    return feedback;
  }

  public void setFeedback(String feedback) {
    this.feedback = feedback;
  }

  public void validate(BindingResult bindingResult) {
    if (username.equals(defaultUsername)) {
      bindingResult.addError(new FieldError("LoginForm", "username",
          "Username must be different than the example!"));
    }
    if (password.equals(defaultPassword)) {
      bindingResult.addError(new FieldError("LoginForm", "password",
          "Password must be different than the example!"));
    }
    if (username.length() < UserUtil.MIN_USERNAME || username.length() > UserUtil.MAX_USERNAME) {
      bindingResult.addError(new FieldError("RegisterUserForm", "username",
          "Username must be at least " + UserUtil.MIN_USERNAME +
            " characters and no longer than " + UserUtil.MAX_USERNAME + " characters."));
    }
    if (username.matches(".*\\s.*")) {
      bindingResult.addError(new FieldError("RegisterUserForm", "username",
          "Username must not contain spaces, tabs, or line breaks."));
    }
    if (password.length() < UserUtil.MIN_PASSWORD || password.length() > UserUtil.MAX_PASSWORD) {
      bindingResult.addError(new FieldError("RegisterUserForm", "password",
          "Password must be between " + UserUtil.MIN_PASSWORD +
            " and " + UserUtil.MAX_PASSWORD + " characters long."));
    }
  }
}
