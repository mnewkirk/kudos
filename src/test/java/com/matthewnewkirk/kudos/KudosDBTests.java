package com.matthewnewkirk.kudos;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import com.matthewnewkirk.kudos.containers.CompletedKudo;
import com.matthewnewkirk.kudos.containers.Kudo;
import com.matthewnewkirk.kudos.containers.KudoText;
import com.matthewnewkirk.kudos.containers.User;
import com.matthewnewkirk.kudos.db.KudoService;
import com.matthewnewkirk.kudos.db.KudoTextService;
import com.matthewnewkirk.kudos.db.ReportingService;
import com.matthewnewkirk.kudos.db.TestDatabaseCleaner;
import com.matthewnewkirk.kudos.db.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@ContextConfiguration(locations =  "classpath*:spring-test.xml")
@TestExecutionListeners(inheritListeners = false, listeners =
  {DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class})
public class KudosDBTests extends AbstractTransactionalTestNGSpringContextTests {

  @Autowired
  private UserService userService;

  @Autowired
  private KudoTextService kudoTextService;

  @Autowired
  private KudoService kudoService;

  @Autowired
  private ReportingService reportingService;

  @Autowired
  private TestDatabaseCleaner testDatabaseCleaner;

  User bob = new User(0, "bob", "bob@example.com");
  User sue = new User(0, "sue", "sue@example.com");
  User alice = new User(0, "alice", "alice@example.com");
  KudoText kudoText = new KudoText("Awesome job building the kudo system!");

  @BeforeClass
  public void createThings() {
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
    User foundBob = userService.findOrAdd(bob);
    Assert.assertEquals(foundBob, bob, "We 'found' bob.");
    Assert.assertTrue(foundBob != bob, "Our 'found' bob is not the same object as bob.");
  }

	@Test ()
	public void testUniqueEmail() {
    User notAlice = new User(0, "notAlice", "alice@example.com");
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
    Assert.assertTrue(foundKudo.getUsersTo().size() == 2, "Kudo found did not have the correct number of To users.");
    Assert.assertEquals(foundKudo.getUserFrom(), bob, "Kudo found was not from Bob.");
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
    Assert.assertTrue(foundKudo.getUsersTo().size() == 2, "Kudo found did not have the correct number of To users.");
    Assert.assertEquals(foundKudo.getUserFrom(), bob, "Kudo found was not from Bob.");
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
    Assert.assertTrue(foundKudo.getUsersTo().size() == 2, "Kudo found did not have the correct number of To users.");
    Assert.assertEquals(foundKudo.getUserFrom(), bob, "Kudo found was not from Bob.");
    Assert.assertEquals(foundKudo.getText(), kudoText, "Kudo found was not of the correct text.");
  }
  @Test
  public void findNoKudosSince() {
    Assert.assertTrue(reportingService.findKudosSinceTime(new Date()).isEmpty(),
      "Somehow kudos were created at the same instant as our search!");
  }
  @AfterClass
  public void cleanUp() throws SQLException {
    logger.info("running cleanUp");
    testDatabaseCleaner.deleteInsertedItems();
  }

}
