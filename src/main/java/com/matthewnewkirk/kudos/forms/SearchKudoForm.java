package com.matthewnewkirk.kudos.forms;

import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

/**
 * @author Matt Newkirk 11/21/2015
 */
public class SearchKudoForm {
  public static int defaultNumberKudos = 100;
  @Pattern(regexp = "[1-9]+[0-9]*", message = "Minimum number of kudos is 1.")
  private String limitNumberOfKudosDisplayed;

  public String getLimitNumberOfKudosDisplayed() {
    return limitNumberOfKudosDisplayed;
  }

  public void setLimitNumberOfKudosDisplayed(String limitNumberOfKudosDisplayed) {
    this.limitNumberOfKudosDisplayed = limitNumberOfKudosDisplayed;
  }

  public SearchKudoForm(String limitNumberOfKudosDisplayed) {
    this.limitNumberOfKudosDisplayed = limitNumberOfKudosDisplayed;
  }

  public SearchKudoForm() {
    limitNumberOfKudosDisplayed = String.valueOf(defaultNumberKudos);
  }
}
