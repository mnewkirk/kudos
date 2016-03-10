package com.matthewnewkirk.kudos.forms;

import com.matthewnewkirk.kudos.util.UserUtil;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

/**
 * @author Matt Newkirk 1/21/2016
 */
public class RegisterUserForm {
  public static final String DEFAULT_USERNAME = "username";
  public static final String DEFAULT_EMAIL = "e-mail@address.com";
  public static final String EMAIL_REGEX = "[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}";
  String username;
  String email;
  String password;
  String confirmationPassword;
  String feedback;

  public RegisterUserForm(String username, String email, String password, String confirmationPassword) {
    this.username = username;
    this.email = email;
    this.password = password;
    this.confirmationPassword = confirmationPassword;
    this.feedback = "";
  }

  public RegisterUserForm() {
    username = DEFAULT_USERNAME;
    email = DEFAULT_EMAIL;
    password = "";
    confirmationPassword = "";
    feedback = "";
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getConfirmationPassword() {
    return confirmationPassword;
  }

  public void setConfirmationPassword(String confirmationPassword) {
    this.confirmationPassword = confirmationPassword;
  }

  public String getFeedback() {
    return feedback;
  }

  public void setFeedback(String feedback) {
    this.feedback = feedback;
  }

  public void validate(BindingResult bindingResult) {
    if (username.equals(DEFAULT_USERNAME)) {
      bindingResult.addError(new FieldError("RegisterUserForm", "username",
          "Username must be different than the example!"));
    }
    if (username.length() < UserUtil.MIN_USERNAME ||
      username.length() > UserUtil.MAX_USERNAME) {
      bindingResult.addError(new FieldError("RegisterUserForm", "username",
          "Username must be at least " + UserUtil.MIN_USERNAME +
            " characters and no longer than " + UserUtil.MAX_USERNAME + " characters."));
    }
    if (username.matches(".*\\s.*")) {
      bindingResult.addError(new FieldError("RegisterUserForm", "username",
          "Username must not contain spaces, tabs, or line breaks."));
    }
    if (email.equals(DEFAULT_EMAIL)) {
      bindingResult.addError(new FieldError("RegisterUserForm", "email",
          "You must supply your e-mail!"));
    }
    if (!email.matches(EMAIL_REGEX)) {
      bindingResult.addError(new FieldError("RegisterUserForm", "email",
          "Email must be in the form of <name>@<domain>."));
    }
    if (password.length() < UserUtil.MIN_PASSWORD ||
      password.length() > UserUtil.MAX_PASSWORD) {
      bindingResult.addError(new FieldError("RegisterUserForm", "password",
          "Password must be between " + UserUtil.MIN_PASSWORD +
            " and " + UserUtil.MAX_PASSWORD + " characters long."));
    }
    if (!password.equals(confirmationPassword)) {
      bindingResult.addError(new FieldError("RegisterUserForm", "confirmationPassword",
          "Confirmation password must be equivalent to the password."));
    }
    if (password.equals(username)) {
      bindingResult.addError(new FieldError("RegisterUserForm", "password",
          "Password must be different than the username!"));
    }
  }
}
