package com.matthewnewkirk.kudos.containers;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author Matt Newkirk 11/7/2015
 */
public class  CompletedKudo {
  private int kudoId;
  private KudoText text;
  private KudoUser kudoUserFrom;
  private List<KudoUser> kudoUsersTo;
  private Date date;

  public CompletedKudo(int kudoId, KudoText text, KudoUser kudoUserFrom, List<KudoUser> kudoUsersTo, Date date) {
    this.kudoId = kudoId;
    this.text = text;
    this.kudoUserFrom = kudoUserFrom;
    this.kudoUsersTo = kudoUsersTo;
    this.date = date;
  }

  public KudoText getText() {
    return text;
  }

  public KudoUser getKudoUserFrom() {
    return kudoUserFrom;
  }

  public List<KudoUser> getKudoUsersTo() {
    return kudoUsersTo;
  }

  public Date getDate() {
    return date;
  }
}
