package com.matthewnewkirk.kudos.util;

import com.matthewnewkirk.kudos.db.UserService;
import com.matthewnewkirk.kudos.forms.AddKudoForm;

/**
 * @author Matt Newkirk 3/9/2016
 */
public class UpdateUtil {
  /**
   * We only want to update the user list if the user list has changed. Because
   * we'll otherwise pass around the same AddKudoForm, it's important to keep
   * this updated.
   */
  public static void updateAvailableUsersList(AddKudoForm addKudoForm, UserService userService) {
    if (addKudoForm.getUserVersion() == userService.getUserVersion()) {
      return;
    }
    addKudoForm.setAvailableKudoUsers(userService.findAllUsers());
    addKudoForm.setUserVersion(userService.getUserVersion());
  }
}
