package com.matthewnewkirk.kudos.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.matthewnewkirk.kudos.containers.KudoUser;

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
  public final static String UNNECESSARY = "unnecessary";
  private final static Logger log = LoggerFactory.getLogger(UserService.class);
  private long userVersion = 0L;

  private JdbcTemplate jdbcTemplate;

  @Autowired
  DatabaseAuditor databaseAuditor;

  @Autowired
  public void setDataSource(DataSource dataSource) {
    this.jdbcTemplate = new JdbcTemplate(dataSource);
  }


  @Transactional
  public boolean add(KudoUser kudoUser) {
    log.debug("Adding " + kudoUser.getUsername() + ", " + kudoUser.getEmail());
    if (findUserByUsername(kudoUser.getUsername()) != null) {
      return false;
    }
    userVersion++;
    SimpleJdbcInsert simpleJdbcInsert =
      new SimpleJdbcInsert(jdbcTemplate)
        .withTableName(USER_TABLE)
        .usingGeneratedKeyColumns(USER_ID);
    Map<String, Object> parameters = new HashMap<>();
    parameters.put(USER_NAME, kudoUser.getUsername());
    parameters.put(USER_EMAIL, kudoUser.getEmail());
    parameters.put(USER_PASSWORD, kudoUser.getHashedPassword());
    Number id = simpleJdbcInsert.executeAndReturnKey(parameters);
    kudoUser.setUserId(id.intValue());
    databaseAuditor.observeIdCreated(USER_TABLE, USER_ID, id.intValue());
    return true;
  }

  public KudoUser findUserByUsername(String username) {
    try {
      return jdbcTemplate.queryForObject(
        "select " + USER_ID + ", " + USER_NAME + ", " +
          USER_EMAIL + "\n" +
          " from " + USER_TABLE + "\n" +
          " where " + USER_NAME + " = ?", new Object[]{username}, (rs, rowNum) -> {
            return new KudoUser(rs.getInt(USER_ID), rs.getString(USER_NAME),
              rs.getString(USER_EMAIL), UNNECESSARY);
          });
    }
    catch (EmptyResultDataAccessException ex) {
      return null;
    }
  }

  public KudoUser findUserById(int id) {
    try {
      return jdbcTemplate.queryForObject(
        "select " + USER_ID + ", " + USER_NAME + ", " +
          USER_EMAIL + "\n" +
          " from " + USER_TABLE + "\n" +
          " where " + USER_ID + " = ?", new Object[]{id}, (rs, rowNum) -> {
            return new KudoUser(rs.getInt(USER_ID), rs.getString(USER_NAME),
              rs.getString(USER_EMAIL), UNNECESSARY);
          });
    }
    catch (EmptyResultDataAccessException ex) {
      return null;
    }
  }

  public KudoUser findUserByUsernameAndPassword(String username, String rawPassword) {
    try {
      KudoUser foundKudoUser = jdbcTemplate.queryForObject(
        "select " + USER_ID + ", " + USER_NAME + ", " +
          USER_EMAIL + ", " + USER_PASSWORD + "\n" +
          " from " + USER_TABLE + "\n" +
          " where " + USER_NAME + " = ?", new Object[]{username}, (rs, rowNum) -> {
          return new KudoUser(rs.getInt(USER_ID), rs.getString(USER_NAME),
            rs.getString(USER_EMAIL), rs.getString(USER_PASSWORD));
        });
      BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
      if (!passwordEncoder.matches(rawPassword, foundKudoUser.getHashedPassword())) {
        return null;
      }
      return foundKudoUser;
    }
    catch (EmptyResultDataAccessException ex) {
      return null;
    }
  }

  public KudoUser findOrAdd(KudoUser kudoUser) {
    KudoUser newKudoUser = findUserByUsername(kudoUser.getUsername());
    if (newKudoUser == null) {
        add(kudoUser);
      newKudoUser = kudoUser;
    }
    return newKudoUser;
  }

  public List<KudoUser> findAllUsers() {
    try {
      String query = "select " + USER_ID + ", " + USER_NAME + ", " +
        USER_EMAIL + "\n" +
        " from " + USER_TABLE + "\n";
      return jdbcTemplate.query(query,
        (rs, rowNum) -> {
          return new KudoUser(rs.getInt(USER_ID), rs.getString(USER_NAME),
            rs.getString(USER_EMAIL), UNNECESSARY);
        });
    }
    catch (EmptyResultDataAccessException ex) {
      return new ArrayList<>();
    }
  }

  public long getUserVersion() {
    return userVersion;
  }

  public static String createUserTableQuery() {
    return "CREATE TABLE IF NOT EXISTS \n" +
      USER_TABLE + "(\n" +
      USER_ID  + " INT NOT NULL AUTO_INCREMENT,\n" +
      USER_NAME + " VARCHAR(100) NOT NULL,\n" +
      USER_EMAIL + " VARCHAR(100) NOT NULL,\n" +
      USER_PASSWORD + " VARCHAR(100) NOT NULL,\n" +
      "PRIMARY KEY (" + USER_ID + "),\n" +
      "UNIQUE INDEX " + USER_NAME + " (" + USER_NAME + " ASC),\n" +
      "INDEX " + USER_EMAIL + " (" + USER_EMAIL + " ASC) );\n";
  }


}

