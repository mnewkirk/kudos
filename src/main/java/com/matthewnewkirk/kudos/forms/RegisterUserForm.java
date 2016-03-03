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
  @NotEmpty(message = "Username may not be empty.")
  String username;
  @NotEmpty(message = "E-mail address must not be empty.")
  String email;
  String feedback;

  public RegisterUserForm(String username, String email) {
    this.username = username;
    this.email = email;
    this.feedback = "";
  }

  public RegisterUserForm() {
    username = defaultUsername;
    email = defaultEmail;
    feedback = "";
  }

  public String getUsername() {
    return username;
  }

  public String getEmail() {
    return email;
  }


  public void setUsername(String username) {
    this.username = username;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getFeedback() {
    return feedback;
  }

  public void setFeedback(String feedback) {
    this.feedback = feedback;
  }

  public boolean validateDefaultsNoErrors() {
    return (!username.equals(defaultUsername) &&
      !email.equals(defaultEmail));
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
  }
}
