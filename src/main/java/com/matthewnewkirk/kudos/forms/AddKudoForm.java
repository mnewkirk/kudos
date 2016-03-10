package com.matthewnewkirk.kudos.forms;

import java.util.ArrayList;
import java.util.List;

import com.matthewnewkirk.kudos.containers.KudoUser;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

/**
 * @author Matt Newkirk 1/21/2016
 */
public class AddKudoForm {
  public static final String defaultText = "<Something you're happy about!>";
  String text;
  String userFrom;
  String userTo;
  String feedback;
  List<KudoUser> availableKudoUsers;
  long userVersion = -1L;

  public AddKudoForm(String kudoText, String userFrom, String userTo) {
    this.text = kudoText;
    this.userFrom = userFrom;
    this.userTo = userTo;
    availableKudoUsers = new ArrayList<>();
  }

  public AddKudoForm() {
    text = defaultText;
    availableKudoUsers = new ArrayList<>();
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

  public String getFeedback() {
    return feedback;
  }

  public void setFeedback(String feedback) {
    this.feedback = feedback;
  }

  public List<KudoUser> getAvailableKudoUsers() {
    return availableKudoUsers;
  }

  public void setAvailableKudoUsers(List<KudoUser> availableKudoUsers) {
    this.availableKudoUsers = availableKudoUsers;
  }

  public long getUserVersion() {
    return userVersion;
  }

  public void setUserVersion(long userVersion) {
    this.userVersion = userVersion;
  }

  public void validate(BindingResult bindingResult) {
    if (text.equals(defaultText)) {
      bindingResult.addError(new FieldError("KudoForm", "text",
          "Kudo text must be different than the example!"));
    }
    if (text.isEmpty()) {
      bindingResult.addError(new FieldError("KudoForm", "text",
          "Kudo text must not be empty!"));
    }

  }
}
