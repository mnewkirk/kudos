package com.matthewnewkirk.kudos.db;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * This cleaner takes note of different table IDs to clean out after tests have completed.
 * This class is autowired in to tests via spring-test.xml
 * @author mnewkirk
 *         created 10/14/2015
 */
public class TestDatabaseCleaner implements DatabaseAuditor {
  private static final Logger log = LoggerFactory.getLogger(TestDatabaseCleaner.class);

  private JdbcTemplate jdbcTemplate;

  HashMap<String, Set<Integer>> itemsToBeDeleted;
  HashMap<String, String> itemKeywords;
  public TestDatabaseCleaner() {
    log.info("TestDatabaseCleaner created");
    itemsToBeDeleted = new HashMap<>();
    itemKeywords = new HashMap<>();
  }

  @Autowired
  public void setDataSource(DataSource dataSource) {
    this.jdbcTemplate = new JdbcTemplate(dataSource);
  }

  @Override
  public void observeIdCreated(String table, String idKeyword, int id) {
    log.debug("Observing the addition of " + table + " : " + id);
    itemKeywords.put(table, idKeyword);
    if (itemsToBeDeleted.get(table) == null) {
      itemsToBeDeleted.put(table, new HashSet<>());
    }
    itemsToBeDeleted.get(table).add(id);
  }

  /**
   * We accumulate a slew of insertions, so now we need to delete them.
   */
  public void deleteInsertedItems() {
    log.debug("Deleting inserted items.");
    for (String tableName : itemsToBeDeleted.keySet()) {
      String itemKeyword = itemKeywords.get(tableName);
      String delete = "DELETE FROM " + tableName + " WHERE " + itemKeyword + " = ?";
      for (Integer id : itemsToBeDeleted.get(tableName)) {
        log.debug("TestExceptionLogDatabaseController: " + delete + " (" + id + ")");
        log.debug(jdbcTemplate.update(delete, id) + " rows deleted.");
      }
    }
    itemsToBeDeleted.clear();
  }

}
