package com.matthewnewkirk.kudos;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.matthewnewkirk.kudos.containers.KudoUser;
import com.matthewnewkirk.kudos.db.UserService;
import com.matthewnewkirk.kudos.forms.LoginForm;
import com.matthewnewkirk.kudos.forms.RegisterUserForm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author Matt Newkirk 11/21/2015
 */
@Controller
public class UserController {
  @Autowired
  private UserService userService;

  @RequestMapping(method = RequestMethod.POST, value = "/register")
  public String registerUser(
    Model model, @ModelAttribute @Valid RegisterUserForm registerUserForm,
    BindingResult bindingResult)
  {
    registerUserForm.validate(bindingResult);
    if (!bindingResult.hasErrors()) {
      KudoUser newKudoUser = new KudoUser(
        0, registerUserForm.getUsername(), registerUserForm.getEmail());
      newKudoUser.setHashedPasswordFromRawPassword(registerUserForm.getPassword());
      if (userService.findUserByUsername(registerUserForm.getUsername()) != null) {
        bindingResult.addError(
          new FieldError(
            "registerUserForm", "username", "A user already exists with this username."));
      }
      else {
        userService.add(newKudoUser);
        registerUserForm.setFeedback(
          "User " + registerUserForm.getUsername() + " has been registered.");
      }
    }
    model.addAttribute(registerUserForm);
    return "register";
  }

  @RequestMapping(method = RequestMethod.GET, value = "/register")
  public String landAtRegister(Model model,
                               @ModelAttribute @Valid RegisterUserForm registerUserForm)
  {
    if (!model.containsAttribute("registerUserForm")) {
      model.addAttribute(registerUserForm);
    }
    return "register";
  }

  @RequestMapping(method = RequestMethod.POST, value = "/login")
  public String login(Model model,
                      @ModelAttribute @Valid LoginForm loginForm,
                      BindingResult bindingResult)
  {
    String pageToReturn = "login";
    loginForm.validate(bindingResult);
    if (!bindingResult.hasErrors()) {
      KudoUser foundKudoUser =
        userService.findUserByUsernameAndPassword(loginForm.getUsername(),
          loginForm.getPassword());
      if (foundKudoUser == null) {
        loginForm.setFeedback("No user could be found with that username and password.");
      }
      else {
        loginForm.setFeedback("User " + foundKudoUser.getUsername() + " logged in.");
      }
    }
    model.addAttribute(loginForm);
    return pageToReturn;
  }

  @RequestMapping(method = RequestMethod.GET, value = "/login")
  public String landAtLogin(Model model,
                               @ModelAttribute @Valid LoginForm loginForm)
  {
    if (!model.containsAttribute("loginForm")) {
      model.addAttribute(loginForm);
    }
    return "login";
  }

  @RequestMapping(method = RequestMethod.POST, value = "/logout")
  public String logout(HttpServletRequest request)
  {
    try {
      request.logout();
    }
    catch (ServletException ex) {
      ex.printStackTrace();
    }
    return "login";
  }
}
