package com.matthewnewkirk.kudos.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This auditor is called when we insert data to various tables.
 */
public interface DatabaseAuditor {
  void observeIdCreated(String table, String idKeyword, int id);

  void deleteInsertedItems();
}
