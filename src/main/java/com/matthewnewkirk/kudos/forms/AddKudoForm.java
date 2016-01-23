package com.matthewnewkirk.kudos.forms;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

/**
 * @author Matt Newkirk 1/21/2016
 */
public class AddKudoForm {
  public static final String defaultText = "<Something you're happy about!>";
  public static final String defaultUserFrom = "<Your name>";
  public static final String defaultUserTo = "<The recipient's name>";
  @NotEmpty(message = "Kudo text may not be empty.")
  String text;
  @NotEmpty(message = "Sender must not be empty.")
  String userFrom;
  @NotEmpty(message = "Recipient must not be empty.")
  String userTo;

  public AddKudoForm(String kudoText, String userFrom, String userTo) {
    this.text = kudoText;
    this.userFrom = userFrom;
    this.userTo = userTo;
  }

  public AddKudoForm() {
    text = defaultText;
    userFrom = defaultUserFrom;
    userTo = defaultUserTo;
  }

  public String getText() {
    return text;
  }

  public String getUserFrom() {
    return userFrom;
  }

  public String getUserTo() {
    return userTo;
  }

  public void setText(String text) {
    this.text = text;
  }

  public void setUserFrom(String userFrom) {
    this.userFrom = userFrom;
  }

  public void setUserTo(String userTo) {
    this.userTo = userTo;
  }

  public void validate(BindingResult bindingResult) {
    if (text.equals(defaultText)) {
      bindingResult.addError(new FieldError("KudoForm", "text", "Kudo text must be different than the example!"));
    }
    if (userFrom.equals(defaultUserFrom)) {
      bindingResult.addError(new FieldError("KudoForm", "userFrom", "You must supply your name in the 'from' field."));
    }
    if (userTo.equals(defaultUserTo)) {
      bindingResult.addError(new FieldError("KudoForm", "userTo", "You must supply the recipient's name in the 'to' field."));
    }
  }
}
