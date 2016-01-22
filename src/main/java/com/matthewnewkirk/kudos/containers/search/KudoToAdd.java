package com.matthewnewkirk.kudos.containers.search;

/**
 * @author Matt Newkirk 1/21/2016
 */
public class KudoToAdd {
  String text;
  String userFrom;
  String userTo;

  public KudoToAdd(String kudoText, String userFrom, String userTo) {
    this.text = kudoText;
    this.userFrom = userFrom;
    this.userTo = userTo;
  }

  public KudoToAdd() {
    text = "for being super helpful!";
    userFrom = "you@example.com";
    userTo = "bob@example.com";
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
}
