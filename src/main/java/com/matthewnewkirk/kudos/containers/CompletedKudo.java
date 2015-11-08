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
}
