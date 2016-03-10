package com.matthewnewkirk.kudos;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import com.matthewnewkirk.kudos.containers.CompletedKudo;
import com.matthewnewkirk.kudos.containers.Kudo;
import com.matthewnewkirk.kudos.containers.KudoText;
import com.matthewnewkirk.kudos.containers.KudoUser;
import com.matthewnewkirk.kudos.db.DatabaseAuditor;
import com.matthewnewkirk.kudos.db.KudoService;
import com.matthewnewkirk.kudos.db.KudoTextService;
import com.matthewnewkirk.kudos.db.ReportingService;
import com.matthewnewkirk.kudos.db.TestH2Manager;
import com.matthewnewkirk.kudos.db.UserService;

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
public class KudosDBTests extends AbstractTransactionalTestNGSpringContextTests {
  @Autowired
  private DatabaseAuditor testDatabaseCleaner;

  @Autowired
  private UserService userService;

  @Autowired
  private KudoTextService kudoTextService;

  @Autowired
  private KudoService kudoService;

  @Autowired
  private ReportingService reportingService;

  @Autowired
  private TestH2Manager testH2Manager;

  KudoUser bob = new KudoUser(0, "bob", "bob@example.com", "test");
  KudoUser sue = new KudoUser(0, "sue", "sue@example.com", "test");
  KudoUser alice = new KudoUser(0, "alice", "alice@example.com", "test");
  KudoText kudoText = new KudoText("Awesome job building the kudo system!");

  @BeforeClass
  public void createThings() {
    testH2Manager.deleteTestDatabases();
    testH2Manager.createTestDatabases();
    Assert.assertTrue(userService.add(bob));
    userService.findOrAdd(sue);
    userService.findOrAdd(alice);
    kudoTextService.add(kudoText);
    Date now = new Date();
    kudoService.add(new Kudo(0, kudoText.getTextId(), bob.getUserId(), sue.getUserId(), now));
    kudoService.add(new Kudo(0, kudoText.getTextId(), bob.getUserId(), alice.getUserId(), now));
  }
  @Test
  public void findOrAdd() {
    KudoUser foundBob = userService.findOrAdd(bob);
    Assert.assertEquals(foundBob, bob, "We 'found' bob.");
    Assert.assertTrue(foundBob != bob, "Our 'found' bob is not the same object as bob.");
  }

	@Test ()
	public void testUniqueUsername() {
    KudoUser notAlice = new KudoUser(0, "alice", "notAlice@example.com", "test");
    Assert.assertFalse(userService.add(notAlice));
  }

  @Test
  public void findKudoTextLike() {
    Assert.assertNotNull(kudoTextService.findKudoTextLike("%kudo system%"));
  }

  @Test
  public void findKudosFor() {
    List<CompletedKudo> foundKudos = reportingService.findKudosFor(alice);
    Assert.assertTrue(foundKudos.size() > 0, "Could not find a kudo for Alice.");
    CompletedKudo foundKudo = foundKudos.get(0);
    Assert.assertTrue(foundKudo.getKudoUsersTo().size() == 2, "Kudo found did not have the correct number of To users.");
    Assert.assertEquals(foundKudo.getKudoUserFrom(), bob, "Kudo found was not from Bob.");
    Assert.assertEquals(foundKudo.getText(), kudoText, "Kudo found was not of the correct text.");
  }

  @Test
  public void findNoKudosFor() {
    Assert.assertTrue(reportingService.findKudosFor(bob).isEmpty(), "Bob had a kudo but shouldn't have.");
  }

  @Test
  public void findKudosFrom() {
    List<CompletedKudo> foundKudos = reportingService.findKudosFrom(bob);
    Assert.assertTrue(foundKudos.size() > 0, "Could not find a kudo from Bob.");
    CompletedKudo foundKudo = foundKudos.get(0);
    Assert.assertTrue(foundKudo.getKudoUsersTo().size() == 2, "Kudo found did not have the correct number of To users.");
    Assert.assertEquals(foundKudo.getKudoUserFrom(), bob, "Kudo found was not from Bob.");
    Assert.assertEquals(foundKudo.getText(), kudoText, "Kudo found was not of the correct text.");
  }
  @Test
  public void findNoKudosFrom() {
    Assert.assertTrue(reportingService.findKudosFrom(sue).isEmpty(), "Sue gave a kudo but shouldn't have.");
  }
  @Test
  public void findKudosSince() {
    List<CompletedKudo> foundKudos = reportingService.findKudosSinceTime(new Date(1));
    Assert.assertTrue(foundKudos.size() > 0, "Could not find a kudo for Alice.");
    CompletedKudo foundKudo = foundKudos.get(0);
    Assert.assertTrue(foundKudo.getKudoUsersTo().size() == 2, "Kudo found did not have the correct number of To users.");
    Assert.assertEquals(foundKudo.getKudoUserFrom(), bob, "Kudo found was not from Bob.");
    Assert.assertEquals(foundKudo.getText(), kudoText, "Kudo found was not of the correct text.");
  }
  @Test
  public void findNoKudosSince() {
    Assert.assertTrue(reportingService.findKudosSinceTime(new Date()).isEmpty(),
      "Somehow kudos were created at the same instant as our search!");
  }
  @Test
  public void cantFindKudoText() {
    Assert.assertNull(kudoTextService.findKudoTextById(-1));
    Assert.assertNull(kudoTextService.findKudoTextLike(""));
  }
  @Test
  public void cantFindKudos() {
    Assert.assertTrue(reportingService.findAllToUsersForSameKudoText(-1).isEmpty());
    Assert.assertTrue(kudoService.findKudosGiven(KudoService.KUDO_TIME, "<", "0000-01-01 00:00:00").isEmpty());
  }
  @Test
  public void cantFindUser() {
    Assert.assertNull(userService.findUserByUsername(""));
    Assert.assertNull(userService.findUserById(-1));
  }
  @AfterClass
  public void cleanUp() throws SQLException {
    logger.info("running cleanUp");
    testDatabaseCleaner.deleteInsertedItems();
  }

}
