package com.matthewnewkirk.kudos.db;

import java.sql.SQLException;

import com.matthewnewkirk.kudos.containers.KudoUser;
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
public class KudoUserService_Test extends AbstractTransactionalTestNGSpringContextTests {
  @Autowired
  private DatabaseAuditor testDatabaseCleaner;

  @Autowired
  private UserService userService;

  @Autowired
  private TestH2Manager testH2Manager;

  KudoUser bob = new KudoUser(0, "bob", "bob@example.com");
  KudoUser sue = new KudoUser(0, "sue", "sue@example.com");
  KudoUser alice = new KudoUser(0, "alice", "alice@example.com");
  String validPassword = new String(new char[UserUtil.MIN_PASSWORD]).replaceAll("\0", "a");

  @BeforeClass
  public void createThings() {
    testH2Manager.deleteTestDatabases();
    testH2Manager.createTestDatabases();
    bob.setHashedPasswordFromRawPassword(validPassword);
    sue.setHashedPasswordFromRawPassword(validPassword);
    alice.setHashedPasswordFromRawPassword(validPassword);
    long userVersion = userService.getUserVersion();
    Assert.assertTrue(userService.add(bob));
    Assert.assertNotEquals(userVersion, userService.getUserVersion(),
      "The userVersion should have incremented on adding bob.");
    userService.findOrAdd(sue);

  }

  @Test
  public void authenticateUser() {
    String username = "aloysius";
    KudoUser kudoUser = new KudoUser(0, username, username + "@example.com");
    kudoUser.setHashedPasswordFromRawPassword(validPassword);
    Assert.assertTrue(
      userService.add(kudoUser), "Adding " + username + " should have succeeded.");
    Assert.assertNotNull(
      userService.findUserByUsernameAndPassword(username, validPassword),
        "We should have found " + username + " by username and password.");
  }

  @Test
  public void findAllUsers() {
    Assert.assertTrue(userService.findAllUsers().size() >= 2,
      "We should have been able to find at least 2 users.");
  }

  @Test
  public void addUniquely() {
    Assert.assertTrue(
      userService.add(alice), "Adding alice should have been successful.");
    Assert.assertNotNull(
      userService.findUserByUsername(alice.getUsername()), "We should have found alice.");
    Assert.assertFalse(
      userService.add(alice), "Adding alice a second time should have been unsuccessful.");
  }

  @Test
  public void findOrAdd() {
    KudoUser foundBob = userService.findOrAdd(bob);
    Assert.assertEquals(foundBob, bob, "We 'found' bob.");
    Assert.assertTrue(foundBob != bob, "Our 'found' bob is not the same object as bob.");
  }

	@Test ()
	public void testUniqueUsername() {
    KudoUser notAlice = new KudoUser(0, "alice", "notAlice@example.com");
    notAlice.setHashedPasswordFromRawPassword("testing");
    Assert.assertFalse(userService.add(notAlice));
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
