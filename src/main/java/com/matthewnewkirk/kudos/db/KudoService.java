package com.matthewnewkirk.kudos.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.matthewnewkirk.kudos.containers.Kudo;
import com.matthewnewkirk.kudos.util.DBUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Matt Newkirk 11/7/2015
 */
public class KudoService {
  public final static String KUDO_TABLE = "kudo";
  public final static String KUDO_ID = "id";
  public final static String KUDO_TEXT_ID = "text_id";
  public final static String KUDO_USER_FROM_ID = "user_from_id";
  public final static String KUDO_USER_TO_ID = "user_to_id";
  public final static String KUDO_TIME = "time";
  private final static Logger log = LoggerFactory.getLogger(KudoService.class);

  private JdbcTemplate jdbcTemplate;

  @Autowired
  DatabaseAuditor databaseAuditor;

  @Autowired
  public void setDataSource(DataSource dataSource) {
    this.jdbcTemplate = new JdbcTemplate(dataSource);
  }


  @Transactional
  public void add(Kudo kudo) {
    log.debug("Adding " + kudo.toString());
    SimpleJdbcInsert simpleJdbcInsert =
      new SimpleJdbcInsert(jdbcTemplate).withTableName(KUDO_TABLE).
        usingGeneratedKeyColumns(KUDO_ID);
    Map<String, Object> parameters = new HashMap<>();
    parameters.put(KUDO_TEXT_ID, kudo.getTextId());
    parameters.put(KUDO_USER_TO_ID, kudo.getUserToId());
    parameters.put(KUDO_USER_FROM_ID, kudo.getUserFromId());
    parameters.put(KUDO_TIME, DBUtil.formatDateAndTimeToString(kudo.getDate()));
    Number id = simpleJdbcInsert.executeAndReturnKey(parameters);
    kudo.setKudoId(id.intValue());
    databaseAuditor.observeIdCreated(KUDO_TABLE, KUDO_ID, id.intValue());
  }

  public List<Integer> findAllToUsersForSameKudoText(int kudoTextId) {
    try {
      return jdbcTemplate.query(
        "select " + KUDO_USER_TO_ID + "\n" +
          " from " + KUDO_TABLE + "\n" +
          " where " + KUDO_TEXT_ID + " = ?", new Object[]{kudoTextId},
        (rs, rowNum) -> {
            return rs.getInt(KUDO_USER_TO_ID);
          });
    }
    catch (EmptyResultDataAccessException ex) {
      return new ArrayList<>();
    }
  }

  public List<Kudo> findKudosGiven(String whereKeyword, String operand, String searchFor) {
    try {
      String query = "select " + KUDO_ID + ", " + KUDO_TEXT_ID + "," +
        KUDO_USER_TO_ID + "," + KUDO_USER_FROM_ID + ", " + KUDO_TIME + "\n" +
        " from " + KUDO_TABLE + "\n" +
        " where " + whereKeyword + " " + operand + " ?";
      return jdbcTemplate.query(query, new Object[]{searchFor}, (rs, rowNum) -> {
        return new Kudo(rs.getInt(KUDO_ID), rs.getInt(KUDO_TEXT_ID),
          rs.getInt(KUDO_USER_FROM_ID),
          rs.getInt(KUDO_USER_TO_ID), rs.getDate(KUDO_TIME));
      });
    }
    catch (EmptyResultDataAccessException ex) {
      return new ArrayList<>();
    }
  }

  public static String createKudoTableQuery() {
    return "CREATE TABLE IF NOT EXISTS \n" +
      KUDO_TABLE + "(\n" +
      KUDO_ID + " INT NOT NULL AUTO_INCREMENT,\n" +
      KUDO_TEXT_ID + " INT NOT NULL,\n" +
      KUDO_USER_FROM_ID + " INT NOT NULL,\n" +
      KUDO_USER_TO_ID + " INT NOT NULL,\n" +
      KUDO_TIME + " DATETIME NOT NULL,\n" +
      "PRIMARY KEY (" + KUDO_ID + "),\n" +
      "INDEX " + KUDO_TEXT_ID + " (" + KUDO_TEXT_ID + " ASC),\n" +
      "INDEX " + KUDO_USER_FROM_ID + " (" + KUDO_USER_FROM_ID + " ASC),\n" +
      "INDEX (" + KUDO_TIME + " ASC),\n" +
      "INDEX " + KUDO_USER_TO_ID + " (" + KUDO_USER_TO_ID + " ASC) );\n";
  }
}

