package com.matthewnewkirk.kudos.containers;

/**
 * @author Matt Newkirk 11/7/2015
 */
public class KudoText {
  private int textId;
  private String text;

  public KudoText(int textId, String text) {
    this.textId = textId;
    this.text = text;
  }

  public KudoText(String text) {
    this.text = text;
  }

  public int getTextId() {
    return textId;
  }

  public void setTextId(int textId) {
    this.textId = textId;
  }

  public String getText() {
    return text;
  }

  @Override
  public String toString() {
    return text;
  }

  @Override
  public boolean equals(Object obj) {
    if (!KudoText.class.isInstance(obj)) {
      return false;
    }
    KudoText otherKudoText = (KudoText) obj;
    return textId == otherKudoText.getTextId() && text.equals(otherKudoText.getText());
  }
}
