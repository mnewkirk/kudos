package com.matthewnewkirk.kudos.containers;

import java.util.List;

import com.matthewnewkirk.kudos.containers.CompletedKudo;
import com.matthewnewkirk.kudos.forms.SearchKudoForm;

/**
 * @author Matt Newkirk 11/21/2015
 */
public class FoundKudos {
  private List<CompletedKudo> foundKudos;
  private SearchKudoForm searchKudoForm;

  public FoundKudos(List<CompletedKudo> foundKudos, SearchKudoForm searchKudoForm) {
    this.foundKudos = foundKudos;
    this.searchKudoForm = searchKudoForm;
  }

  public List<CompletedKudo> getFoundKudos() {
    return foundKudos;
  }

  public SearchKudoForm getSearchKudoForm() {
    return searchKudoForm;
  }

  public String getSearchSize() {
    return searchKudoForm.getLimitNumberOfKudosDisplayed();
  }

  public void setFoundKudos(List<CompletedKudo> foundKudos) {
    this.foundKudos = foundKudos;
  }

  public void setSearchKudoForm(SearchKudoForm searchKudoForm) {
    this.searchKudoForm = searchKudoForm;
  }
}
