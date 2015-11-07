package com.matthewnewkirk.kudos.containers;

import java.util.Date;
import java.util.List;

/**
 * @author Matt Newkirk 11/7/2015
 */
public class CompletedKudo {
  private int kudoId;
  private KudoText text;
  private User userFrom;
  private List<User> usersTo;
  private Date date;

  public CompletedKudo(int kudoId, KudoText text, User userFrom, List<User> usersTo, Date date) {
    this.kudoId = kudoId;
    this.text = text;
    this.userFrom = userFrom;
    this.usersTo = usersTo;
    this.date = date;
  }

  public KudoText getText() {
    return text;
  }

  public User getUserFrom() {
    return userFrom;
  }

  public List<User> getUsersTo() {
    return usersTo;
  }

  public void setKudoId(int kudoId) {
    this.kudoId = kudoId;
  }

  public Date getDate() {
    return date;
  }

  @Override
  public String toString() {
    String output = "Kudo{" +
      "kudoId=" + kudoId +
      ", text='" + text.toString() + '\'' +
      ", userFrom=" + userFrom.getUsername() + " (" + userFrom.getEmail() + ")" +
      ", usersTo=[";
    for (User user : usersTo) {
      output += user.getUserId() + "(" + user.getEmail() + "),";
    }
    output += "]" +
      ", date=" + date.toString() + "}";
    return output;
  }
}
