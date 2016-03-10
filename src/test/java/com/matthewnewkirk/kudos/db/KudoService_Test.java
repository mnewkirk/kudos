package com.matthewnewkirk.kudos.db;

import java.sql.SQLException;

import com.matthewnewkirk.kudos.containers.KudoUser;
import com.matthewnewkirk.kudos.forms.AddKudoForm;
import com.matthewnewkirk.kudos.util.UserUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@WebAppConfiguration
@ContextConfiguration(locations =  "classpath*:spring-test.xml")
@TestExecutionListeners(inheritListeners = false, listeners =
  {DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class})
public class KudoService_Test extends AbstractTransactionalTestNGSpringContextTests {
  @Autowired
  private DatabaseAuditor testDatabaseCleaner;

  @Autowired
  private AddKudoService addKudoService;

  @Autowired
  private UserService userService;

  @Autowired
  private KudoService kudoService;

  @Autowired
  private ReportingService reportingService;

  @Autowired
  private TestH2Manager testH2Manager;

  KudoUser bob = new KudoUser(0, "bob", "bob@example.com");
  KudoUser sue = new KudoUser(0, "sue", "sue@example.com");

  @BeforeClass
  public void createThings() {
    testH2Manager.deleteTestDatabases();
    testH2Manager.createTestDatabases();
    String validPassword = new String(new char[UserUtil.MIN_PASSWORD]).replaceAll("\0", "a");
    bob.setHashedPasswordFromRawPassword(validPassword);
    sue.setHashedPasswordFromRawPassword(validPassword);
    Assert.assertTrue(userService.add(bob));
    userService.add(sue);
  }
  @Test
  public void addKudoFromForm() {
    Assert.assertTrue(kudoService.findLastNKudos(1).isEmpty(),
      "There should not be any kudos found yet.");
    Assert.assertTrue(reportingService.findKudosFor(sue).isEmpty(),
      "There should not be any kudos found for Sue yet.");
    addKudoService.addKudo(
      new AddKudoForm(
        "Awesome job building the kudo system!", bob.getUsername(), sue.getUsername()));
    Assert.assertFalse(
      reportingService.findLastNKudos(1).isEmpty(),
      "The kudo should have been found.");
  }

  @AfterClass
  public void cleanUp() throws SQLException {
    logger.info("running cleanUp");
    testDatabaseCleaner.deleteInsertedItems();
  }
}
