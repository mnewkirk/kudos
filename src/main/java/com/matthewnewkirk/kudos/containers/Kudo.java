package com.matthewnewkirk.kudos.containers;

import java.util.Date;

/**
 * @author Matt Newkirk 11/7/2015
 */
public class Kudo {
  private int kudoId;
  private int textId;
  private int userFromId;
  private int userToId;
  private Date date;

  public Kudo(int kudoId, int textId, int userFromId, int userToId, Date date) {
    this.kudoId = kudoId;
    this.textId = textId;
    this.userFromId = userFromId;
    this.userToId = userToId;
    this.date = date;
  }

  public int getKudoId() {
    return kudoId;
  }

  public int getTextId() {
    return textId;
  }

  public int getUserFromId() {
    return userFromId;
  }

  public int getUserToId() {
    return userToId;
  }

  public void setKudoId(int kudoId) {
    this.kudoId = kudoId;
  }

  public Date getDate() {
    return date;
  }

  @Override
  public String toString() {
    return "Kudo{" +
      "kudoId=" + kudoId +
      ", textId=" + textId +
      ", userFromId=" + userFromId +
      ", userToId=" + userToId +
      ", date=" + date +
      '}';
  }
}
