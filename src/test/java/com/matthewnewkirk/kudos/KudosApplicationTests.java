package com.matthewnewkirk.kudos;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.matthewnewkirk.kudos.containers.Kudo;
import com.matthewnewkirk.kudos.containers.KudoText;
import com.matthewnewkirk.kudos.containers.User;
import com.matthewnewkirk.kudos.db.KudoService;
import com.matthewnewkirk.kudos.db.KudoTextService;
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
public class KudosApplicationTests extends AbstractTransactionalTestNGSpringContextTests {

  @Autowired
  private UserService userService;

  @Autowired
  private KudoTextService kudoTextService;

  @Autowired
  private KudoService kudoService;

  @Autowired
  private TestDatabaseCleaner testDatabaseCleaner;

  User bob = new User(0, "bob", "bob@example.com");
  User sue = new User(0, "sue", "sue@example.com");
  User alice = new User(0, "alice", "alice@example.com");
  KudoText kudoText = new KudoText("Awesome job building the kudo system!");
  Kudo kudo;

  @BeforeClass
  public void createThings() {
    Assert.assertTrue(userService.add(bob));
    Assert.assertTrue(userService.add(sue));
    Assert.assertTrue(userService.add(alice));
    kudoTextService.add(kudoText);
    List<User> usersTo = new ArrayList<>();
    usersTo.add(sue);
    usersTo.add(alice);
    kudo = new Kudo(0, kudoText, bob, usersTo, new Date());
    kudoService.add(kudo);
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
    List<Kudo> foundKudos = kudoService.findKudosFor(alice);
    Assert.assertTrue(foundKudos.size() > 0, "Could not find a kudo for Alice.");
    Kudo foundKudo = foundKudos.get(0);
    Assert.assertTrue(foundKudo.getUsersTo().size() == 2, "Kudo found did not have the correct number of To users.");
    Assert.assertEquals(foundKudo.getUserFrom(), bob, "Kudo found was not from Bob.");
    Assert.assertEquals(foundKudo.getText(), kudoText, "Kudo found was not of the correct text.");
  }

  @Test
  public void findKudosFrom() {
    List<Kudo> foundKudos = kudoService.findKudosFrom(bob);
    Assert.assertTrue(foundKudos.size() > 0, "Could not find a kudo from Bob.");
    Kudo foundKudo = foundKudos.get(0);
    Assert.assertTrue(foundKudo.getUsersTo().size() == 2, "Kudo found did not have the correct number of To users.");
    Assert.assertEquals(foundKudo.getUserFrom(), bob, "Kudo found was not from Bob.");
    Assert.assertEquals(foundKudo.getText(), kudoText, "Kudo found was not of the correct text.");
  }
  @Test
  public void findKudosSince() {
    List<Kudo> foundKudos = kudoService.findKudosSinceTime(new Date(1));
    Assert.assertTrue(foundKudos.size() > 0, "Could not find a kudo for Alice.");
    Kudo foundKudo = foundKudos.get(0);
    Assert.assertTrue(foundKudo.getUsersTo().size() == 2, "Kudo found did not have the correct number of To users.");
    Assert.assertEquals(foundKudo.getUserFrom(), bob, "Kudo found was not from Bob.");
    Assert.assertEquals(foundKudo.getText(), kudoText, "Kudo found was not of the correct text.");
  }
  @AfterClass
  public void cleanUp() throws SQLException {
    logger.info("running cleanUp");
    testDatabaseCleaner.deleteInsertedItems();
  }

}
