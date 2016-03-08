package com.matthewnewkirk.kudos.containers;

/**
 * @author Matt Newkirk 11/7/2015
 */
public class User {
  private int userId;
  private String username;
  private String email;
  private String hashedPassword;

  public User(int userId, String username, String email, String hashedPassword) {
    this.userId = userId;
    this.username = username;
    this.email = email;
    this.hashedPassword = hashedPassword;
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

  public String getHashedPassword() {
    return hashedPassword;
  }

  public void setHashedPassword(String hashedPassword) {
    this.hashedPassword = hashedPassword;
  }

  @Override
  public boolean equals(Object obj) {
    if (!User.class.isInstance(obj)) {
      return false;
    }
    User otherUser = (User) obj;
    return (userId == otherUser.getUserId() &&
    username.equals(otherUser.getUsername()) &&
    email.equals(otherUser.getEmail()));
  }
}
