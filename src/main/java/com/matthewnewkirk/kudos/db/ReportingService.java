package com.matthewnewkirk.kudos.db;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.matthewnewkirk.kudos.containers.CompletedKudo;
import com.matthewnewkirk.kudos.containers.Kudo;
import com.matthewnewkirk.kudos.containers.KudoText;
import com.matthewnewkirk.kudos.containers.User;
import com.matthewnewkirk.kudos.util.DBUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Matt Newkirk 11/7/2015
 */
public class ReportingService {
  private final static Logger log = LoggerFactory.getLogger(ReportingService.class);

  @Autowired
  DatabaseAuditor databaseAuditor;

  @Autowired
  KudoTextService kudoTextService;

  @Autowired
  KudoService kudoService;

  @Autowired
  UserService userService;

  public List<CompletedKudo> findKudosFor(User user) {
    List<Kudo> kudos =
      kudoService.findKudosGiven(KudoService.KUDO_USER_TO_ID, "=",
        String.valueOf(user.getUserId()));
    return completedKudosFromKudos(kudos);
  }

  public List<CompletedKudo> findKudosFrom(User user) {
    List<Kudo> kudos =
      kudoService.findKudosGiven(KudoService.KUDO_USER_FROM_ID, "=",
        String.valueOf(user.getUserId()));
    return completedKudosFromKudos(kudos);
  }

  public List<CompletedKudo> findKudosSinceTime(Date date) {
    List<Kudo> kudos =
      kudoService.findKudosGiven(KudoService.KUDO_TIME, ">=",
        String.valueOf(DBUtil.formatDateAndTimeToString(date)));
    return completedKudosFromKudos(kudos);
  }

  /**
   * Simplify our filling out a CompletedKudo here.
   */
  public List<CompletedKudo> completedKudosFromKudos(List<Kudo> kudos) {
    List<CompletedKudo> completedKudos = new ArrayList<>();
    for (Kudo kudo : kudos) {
      KudoText kudoText = kudoTextService.findKudoTextById(kudo.getTextId());
      List<User> sharedKudos = findAllToUsersForSameKudoText(kudo.getTextId());
      completedKudos.add(new CompletedKudo(kudo.getKudoId(), kudoText,
          userService.findUserById(kudo.getUserFromId()),
          sharedKudos, kudo.getDate()));
    }
    return completedKudos;
  }
  private List<User> findAllToUsersForSameKudoText(int kudoTextId) {
    List<Integer> userIds = kudoService.findAllToUsersForSameKudoText(kudoTextId);
    return userIds.stream()
      .map(userService::findUserById)
      .collect(Collectors.toList());
  }
}
