package com.matthewnewkirk.kudos.db;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import com.matthewnewkirk.kudos.containers.CompletedKudo;
import com.matthewnewkirk.kudos.containers.Kudo;
import com.matthewnewkirk.kudos.containers.KudoText;
import com.matthewnewkirk.kudos.containers.KudoUser;
import com.matthewnewkirk.kudos.util.DBUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author Matt Newkirk 11/7/2015
 */
public class ReportingService {
  private final static Logger log = LoggerFactory.getLogger(ReportingService.class);

  private JdbcTemplate jdbcTemplate;

  @Autowired
  public void setDataSource(DataSource dataSource) {
    this.jdbcTemplate = new JdbcTemplate(dataSource);
  }

  @Autowired
  KudoTextService kudoTextService;

  @Autowired
  KudoService kudoService;

  @Autowired
  UserService userService;

  public List<CompletedKudo> findKudosFor(KudoUser kudoUser) {
    List<Kudo> kudos =
      kudoService.findKudosGiven(KudoService.KUDO_USER_TO_ID, "=",
        String.valueOf(kudoUser.getUserId()));
    return completedKudosFromKudos(kudos);
  }

  public List<CompletedKudo> findKudosFrom(KudoUser kudoUser) {
    List<Kudo> kudos =
      kudoService.findKudosGiven(KudoService.KUDO_USER_FROM_ID, "=",
        String.valueOf(kudoUser.getUserId()));
    return completedKudosFromKudos(kudos);
  }

  public List<CompletedKudo> findKudosSinceTime(Date date) {
    List<Kudo> kudos =
      kudoService.findKudosGiven(
        KudoService.KUDO_TIME, ">=", String.valueOf(DBUtil.formatDateAndTimeToString(date)));
    return completedKudosFromKudos(kudos);
  }

  public List<CompletedKudo> findLastNKudos(int limit) {
    List<Kudo> kudos =
      kudoService.findLastNKudos(limit);
    return completedKudosFromKudos(kudos);
  }

  /**
   * Simplify our filling out a CompletedKudo here.
   */
  public List<CompletedKudo> completedKudosFromKudos(List<Kudo> kudos) {
    List<CompletedKudo> completedKudos = new ArrayList<>();
    for (Kudo kudo : kudos) {
      KudoText kudoText = kudoTextService.findKudoTextById(kudo.getTextId());
      List<KudoUser> sharedKudos = findAllToUsersForSameKudoText(kudo.getTextId());
      completedKudos.add(new CompletedKudo(kudo.getKudoId(), kudoText,
          userService.findUserById(kudo.getUserFromId()),
          sharedKudos, kudo.getDate()));
    }
    return completedKudos;
  }

  public List<KudoUser> findAllToUsersForSameKudoText(int kudoTextId) {
    try {
      return jdbcTemplate.query(
        "select " + UserService.USER_TABLE + "." +
          UserService.USER_ID + ", " + UserService.USER_NAME + ", " +
          UserService.USER_EMAIL + "\n" +
          " from " + UserService.USER_TABLE + "\n" +
          " LEFT JOIN " + KudoService.KUDO_TABLE + "\n" +
          " on " + KudoService.KUDO_USER_TO_ID + " = " +
          UserService.USER_TABLE + "." + UserService.USER_ID + "\n" +
          " where " + KudoService.KUDO_TEXT_ID + " = ?", new Object[]{kudoTextId},
        (rs, rowNum) -> {
          return new KudoUser(rs.getInt(UserService.USER_ID), rs.getString(UserService.USER_NAME),
            rs.getString(UserService.USER_EMAIL), UserService.UNNECESSARY);
        });
    }
    catch (EmptyResultDataAccessException ex) {
      return new ArrayList<>();
    }
  }
}
