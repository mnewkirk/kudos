package com.matthewnewkirk.kudos.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This auditor is called when we insert data to various tables.
 */
public class ProdDatabaseAuditor implements DatabaseAuditor {
  Logger log = LoggerFactory.getLogger(ProdDatabaseAuditor.class);

  public void observeIdCreated(String table, String idKeyword, int id) {
    log.debug("Observing the addition of " + table + " : " + id);
  }

  public void deleteInsertedItems() {
    // Do nothing for production.
  }
}
