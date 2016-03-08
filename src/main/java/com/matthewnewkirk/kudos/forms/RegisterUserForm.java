package com.matthewnewkirk.kudos.forms;

import org.hibernate.validator.constraints.NotEmpty;
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
  String rawPassword;
  String confirmationPassword;
  String feedback;

  public RegisterUserForm(String username, String email, String rawPassword, String confirmationPassword) {
    this.username = username;
    this.email = email;
    this.rawPassword = rawPassword;
    this.confirmationPassword = confirmationPassword;
    this.feedback = "";
  }

  public RegisterUserForm() {
    username = defaultUsername;
    email = defaultEmail;
    rawPassword = "";
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

  public String getRawPassword() {
    return rawPassword;
  }

  public void setRawPassword(String rawPassword) {
    this.rawPassword = rawPassword;
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
    if (rawPassword.length() < 8 || rawPassword.length() > 100) {
      bindingResult.addError(new FieldError("RegisterUserForm", "rawPassword",
          "Password must be between 8 and 100 characters long."));
    }
    if (!rawPassword.equals(confirmationPassword)) {
      bindingResult.addError(new FieldError("RegisterUserForm", "confirmationPassword",
          "Confirmation password must be equivalent to the password."));
    }
    if (rawPassword.equals(defaultUsername)) {
      bindingResult.addError(new FieldError("RegisterUserForm", "rawPassword",
          "Password must be different than the username!"));
    }
  }
}
