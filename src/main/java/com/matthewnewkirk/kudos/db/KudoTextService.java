package com.matthewnewkirk.kudos.db;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import com.matthewnewkirk.kudos.containers.KudoText;

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
public class KudoTextService {
  public final static String TEXT_TABLE = "kudo_text";
  public final static String TEXT_ID = "id";
  public final static String TEXT_KEYWORD = "text";
  private final static Logger log = LoggerFactory.getLogger(KudoTextService.class);

  private JdbcTemplate jdbcTemplate;

  @Autowired
  public void setDataSource(DataSource dataSource) {
    this.jdbcTemplate = new JdbcTemplate(dataSource);
  }

  @Autowired
  DatabaseAuditor databaseAuditor;

  @Transactional
  public void add(KudoText text) {
    log.debug("Adding " + text);
    SimpleJdbcInsert simpleJdbcInsert =
      new SimpleJdbcInsert(jdbcTemplate)
        .withTableName(TEXT_TABLE)
        .usingGeneratedKeyColumns(TEXT_ID);
    Map<String, Object> parameters = new HashMap<>();
    parameters.put(TEXT_KEYWORD, text.getText());
    Number id = simpleJdbcInsert.executeAndReturnKey(parameters);
    text.setTextId(id.intValue());
    databaseAuditor.observeIdCreated(TEXT_TABLE, TEXT_ID, id.intValue());
  }

  public KudoText findKudoTextLike(String text) {
    try {
      return jdbcTemplate.queryForObject(
        "select " + TEXT_ID + ", " + TEXT_KEYWORD + "\n" +
          " from " + TEXT_TABLE + "\n" +
          " where " + TEXT_KEYWORD + " LIKE ?", new Object[]{text}, (rs, rowNum) -> {
            return new KudoText(rs.getInt(TEXT_ID), rs.getString(TEXT_KEYWORD));
          });
    }
    catch (EmptyResultDataAccessException ex) {
      return null;
    }
  }

  public KudoText findKudoTextById(int id) {
    try {
      return jdbcTemplate.queryForObject(
        "select " + TEXT_ID + ", " + TEXT_KEYWORD + "\n" +
          " from " + TEXT_TABLE + "\n" +
          " where " + TEXT_ID + " = ?", new Object[]{id}, (rs, rowNum) -> {
            return new KudoText(rs.getInt(TEXT_ID), rs.getString(TEXT_KEYWORD));
          });
    }
    catch (EmptyResultDataAccessException ex) {
      return null;
    }
  }

  public static String createKudoTextTableQuery() {
    return "CREATE TABLE IF NOT EXISTS \n" +
      TEXT_TABLE + "(\n" +
      TEXT_ID + " INT NOT NULL AUTO_INCREMENT,\n" +
      TEXT_KEYWORD + " MEDIUMTEXT NULL,\n" +
      "PRIMARY KEY (" + TEXT_ID + ") );\n";
  }
}

