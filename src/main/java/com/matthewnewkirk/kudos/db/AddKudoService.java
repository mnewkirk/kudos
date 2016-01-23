package com.matthewnewkirk.kudos.db;

import java.util.Date;

import com.matthewnewkirk.kudos.containers.Kudo;
import com.matthewnewkirk.kudos.containers.KudoText;
import com.matthewnewkirk.kudos.containers.User;
import com.matthewnewkirk.kudos.forms.AddKudoForm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author Matt Newkirk 11/7/2015
 */
public class AddKudoService {
  private final static Logger log = LoggerFactory.getLogger(AddKudoService.class);

  @Autowired
  private KudoService kudoService;
  
  @Autowired
  private KudoTextService kudoTextService;
  
  @Autowired
  private UserService userService;
  
  private JdbcTemplate jdbcTemplate;

  public Kudo addKudo(AddKudoForm addKudoForm) {
    User userFrom = 
      userService.findUserByEmail(addKudoForm.getUserFrom());
    if (userFrom == null) {
      userFrom = new User(0, addKudoForm.getUserFrom(), addKudoForm.getUserFrom());
      userService.add(userFrom);
    }
    User userTo =
      userService.findUserByEmail(addKudoForm.getUserTo());
    if (userTo == null) {
      userTo= new User(0, addKudoForm.getUserTo(), addKudoForm.getUserTo());
      userService.add(userTo);
    }

    KudoText kudoText = new KudoText(addKudoForm.getText());
    kudoTextService.add(kudoText);
    Kudo kudo = new Kudo(0, kudoText.getTextId(), userFrom.getUserId(), userTo.getUserId(), new Date());

    kudoService.add(kudo);
    return kudo;
  }
}

