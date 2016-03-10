package com.matthewnewkirk.kudos.forms;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

/**
 * @author Matt Newkirk 1/21/2016
 */
public class RegisterUserForm {
  public static final String defaultUsername = "username";
  public static final String defaultEmail = "e-mail@address.com";
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
    username = defaultUsername;
    email = defaultEmail;
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
    if (username.equals(defaultUsername)) {
      bindingResult.addError(new FieldError("RegisterUserForm", "username",
          "Username must be different than the example!"));
    }
    if (username.length() < 3 || username.length() > 12) {
      bindingResult.addError(new FieldError("RegisterUserForm", "username",
          "Username must be at least 3 characters and no longer than 12 characters."));
    }
    if (username.matches(".*\\s.*")) {
      bindingResult.addError(new FieldError("RegisterUserForm", "username",
          "Username must not contain spaces, tabs, or line breaks."));
    }
    if (email.equals(defaultEmail)) {
      bindingResult.addError(new FieldError("RegisterUserForm", "email",
          "You must supply your e-mail!"));
    }
    if (!email.matches("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}")) {
      bindingResult.addError(new FieldError("RegisterUserForm", "email",
          "Email must be in the form of <name>@<domain>."));
    }
    if (password.length() < 8 || password.length() > 100) {
      bindingResult.addError(new FieldError("RegisterUserForm", "password",
          "Password must be between 8 and 100 characters long."));
    }
    if (!password.equals(confirmationPassword)) {
      bindingResult.addError(new FieldError("RegisterUserForm", "confirmationPassword",
          "Confirmation password must be equivalent to the password."));
    }
    if (password.equals(defaultUsername)) {
      bindingResult.addError(new FieldError("RegisterUserForm", "password",
          "Password must be different than the username!"));
    }
  }
}
