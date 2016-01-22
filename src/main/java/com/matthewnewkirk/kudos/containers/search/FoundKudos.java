package com.matthewnewkirk.kudos.containers.search;

import java.util.List;

import com.matthewnewkirk.kudos.containers.CompletedKudo;
import com.matthewnewkirk.kudos.containers.Kudo;
import com.matthewnewkirk.kudos.db.KudoService;

/**
 * @author Matt Newkirk 11/21/2015
 */
public class FoundKudos {
  private List<CompletedKudo> foundKudos;
  private KudoSearch kudoSearch;

  public FoundKudos(List<CompletedKudo> foundKudos, KudoSearch kudoSearch) {
    this.foundKudos = foundKudos;
    this.kudoSearch = kudoSearch;
  }

  public List<CompletedKudo> getFoundKudos() {
    return foundKudos;
  }

  public KudoSearch getkudoSearch() {
    return kudoSearch;
  }

  public void setFoundKudos(List<CompletedKudo> foundKudos) {
    this.foundKudos = foundKudos;
  }

  public void setKudoSearch(KudoSearch kudoSearch) {
    this.kudoSearch = kudoSearch;
  }
}
