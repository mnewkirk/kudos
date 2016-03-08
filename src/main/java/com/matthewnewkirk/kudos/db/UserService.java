package com.matthewnewkirk.kudos.db;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import com.matthewnewkirk.kudos.containers.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Matt Newkirk 11/7/2015
 */
public class UserService {
  public final static String USER_TABLE = "user";
  public final static String USER_ID = "id";
  public final static String USER_NAME = "username";
  public final static String USER_EMAIL = "email";
  public final static String USER_PASSWORD = "password";
  private final static Logger log = LoggerFactory.getLogger(UserService.class);

  private JdbcTemplate jdbcTemplate;

  @Autowired
  DatabaseAuditor databaseAuditor;

  @Autowired
  public void setDataSource(DataSource dataSource) {
    this.jdbcTemplate = new JdbcTemplate(dataSource);
  }


  @Transactional
  public boolean add(User user) {
    log.debug("Adding " + user.getUsername() + ", " + user.getEmail());
    if (findUserByEmail(user.getEmail()) != null) {
      return false;
    }
    SimpleJdbcInsert simpleJdbcInsert =
      new SimpleJdbcInsert(jdbcTemplate)
        .withTableName(USER_TABLE)
        .usingGeneratedKeyColumns(USER_ID);
    Map<String, Object> parameters = new HashMap<>();
    parameters.put(USER_NAME, user.getUsername());
    parameters.put(USER_EMAIL, user.getEmail());
    parameters.put(USER_PASSWORD, user.getHashedPassword());
    Number id = simpleJdbcInsert.executeAndReturnKey(parameters);
    user.setUserId(id.intValue());
    databaseAuditor.observeIdCreated(USER_TABLE, USER_ID, id.intValue());
    return true;
  }

  public User findUserByEmail(String email) {
    try {
      return jdbcTemplate.queryForObject(
        "select " + USER_ID + ", " + USER_NAME + ", " +
          USER_EMAIL + ", " + USER_PASSWORD + "\n" +
          " from " + USER_TABLE + "\n" +
          " where " + USER_EMAIL + " = ?", new Object[]{email}, (rs, rowNum) -> {
            return new User(rs.getInt(USER_ID), rs.getString(USER_NAME),
              rs.getString(USER_EMAIL), rs.getString(USER_PASSWORD));
          });
    }
    catch (EmptyResultDataAccessException ex) {
      return null;
    }
  }

  public User findUserById(int id) {
    try {
      return jdbcTemplate.queryForObject(
        "select " + USER_ID + ", " + USER_NAME + ", " +
          USER_EMAIL + ", " + USER_PASSWORD + "\n" +
          " from " + USER_TABLE + "\n" +
          " where " + USER_ID + " = ?", new Object[]{id}, (rs, rowNum) -> {
            return new User(rs.getInt(USER_ID), rs.getString(USER_NAME),
              rs.getString(USER_EMAIL), rs.getString(USER_PASSWORD));
          });
    }
    catch (EmptyResultDataAccessException ex) {
      return null;
    }
  }

  public User findUserByUsernameAndPassword(String username, String rawPassword) {
    try {
      User foundUser = jdbcTemplate.queryForObject(
        "select " + USER_ID + ", " + USER_NAME + ", " +
          USER_EMAIL + ", " + USER_PASSWORD + "\n" +
          " from " + USER_TABLE + "\n" +
          " where " + USER_NAME + " = ?", new Object[]{username}, (rs, rowNum) -> {
          return new User(rs.getInt(USER_ID), rs.getString(USER_NAME),
            rs.getString(USER_EMAIL), rs.getString(USER_PASSWORD));
        });
      BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
      if (!passwordEncoder.matches(rawPassword, foundUser.getHashedPassword())) {
        return null;
      }
      return foundUser;
    }
    catch (EmptyResultDataAccessException ex) {
      return null;
    }
  }

  public User findOrAdd(User user) {
    User newUser = findUserByEmail(user.getEmail());
    if (newUser == null) {
        add(user);
      newUser = user;
    }
    return newUser;

  }

  public static String createUserTableQuery() {
    return "CREATE TABLE IF NOT EXISTS \n" +
      USER_TABLE + "(\n" +
      USER_ID  + " INT NOT NULL AUTO_INCREMENT,\n" +
      USER_NAME + " VARCHAR(100) NOT NULL,\n" +
      USER_EMAIL + " VARCHAR(100) NOT NULL,\n" +
      USER_PASSWORD + " VARCHAR(100) NOT NULL,\n" +
      "PRIMARY KEY (" + USER_ID + "),\n" +
      "INDEX " + USER_NAME + " (" + USER_NAME + " ASC),\n" +
      "UNIQUE INDEX " + USER_EMAIL + " (" + USER_EMAIL + " ASC) );\n";
  }
}

