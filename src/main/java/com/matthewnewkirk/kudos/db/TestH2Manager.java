package com.matthewnewkirk.kudos.db;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * We create our tables in test databases through this datasource.
 * @author mnewkirk
 *         created 10/12/2015
 */
public class TestH2Manager  {
  private static final Logger log = LoggerFactory.getLogger(TestH2Manager.class);
  JdbcTemplate jdbcTemplate;

  @Autowired
  public void setDataSource(DataSource dataSource) {
    this.jdbcTemplate = new JdbcTemplate(dataSource);
  }

  public void deleteTestDatabases() {
    for (String table : new String[] { UserService.USER_TABLE,
      KudoTextService.TEXT_TABLE,
      KudoService.KUDO_TABLE}) {
      log.info("dropping table " + table);
      jdbcTemplate.execute("drop table if exists " + table);
    }
  }
  /**
   * If and only if we're connected to a test_schema, drop and create new tables.
   */
  public void createTestDatabases()  {
    /*
    String error = "Not connected to a test database. NOT performing db management.";
    try {
      if (!jdbcTemplate.getDataSource().getConnection().getMetaData().getURL().contains("test_schema")) {
        log.debug(error);
        return;
      }
    }
    catch (SQLException ex) {
      log.debug(error);
      return;
    }*/
    log.info("Creating tables");
    jdbcTemplate.execute(UserService.createUserTableQuery());
    jdbcTemplate.execute(KudoTextService.createKudoTextTableQuery());
    jdbcTemplate.execute(KudoService.createKudoTableQuery());
  }
}
