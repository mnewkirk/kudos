package com.matthewnewkirk.kudos.containers;

/**
 * @author Matt Newkirk 11/7/2015
 */
public class User {
  private int userId;
  private String username;
  private String email;

  public User(int userId, String username, String email) {
    this.userId = userId;
    this.username = username;
    this.email = email;
  }

  public int getUserId() {
    return userId;
  }

  public String getUsername() {
    return username;
  }

  public String getEmail() {
    return email;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  @Override
  public boolean equals(Object obj) {
    User otherUser = (User) obj;
    return (userId == otherUser.getUserId() &&
    username.equals(otherUser.getUsername()) &&
    email.equals(otherUser.getEmail()));
  }
}
