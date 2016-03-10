package com.matthewnewkirk.kudos.containers;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author Matt Newkirk 11/7/2015
 */
public class KudoUser {
  private int userId;
  private String username;
  private String email;
  private String hashedPassword;

  public KudoUser(int userId, String username, String email) {
    this.userId = userId;
    this.username = username;
    this.email = email;
    hashedPassword = "UNNECESSARY";
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

  public void setHashedPasswordFromRawPassword(String rawPassword) {
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    hashedPassword = passwordEncoder.encode(rawPassword);
  }

  @Override
  public boolean equals(Object obj) {
    if (!KudoUser.class.isInstance(obj)) {
      return false;
    }
    KudoUser otherKudoUser = (KudoUser) obj;
    return (userId == otherKudoUser.getUserId() &&
    username.equals(otherKudoUser.getUsername()) &&
    email.equals(otherKudoUser.getEmail()));
  }
}
