package com.matthewnewkirk.kudos;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import com.matthewnewkirk.kudos.containers.CompletedKudo;
import com.matthewnewkirk.kudos.containers.FoundKudos;
import com.matthewnewkirk.kudos.containers.User;
import com.matthewnewkirk.kudos.db.AddKudoService;
import com.matthewnewkirk.kudos.db.ReportingService;
import com.matthewnewkirk.kudos.db.UserService;
import com.matthewnewkirk.kudos.forms.AddKudoForm;
import com.matthewnewkirk.kudos.forms.RegisterUserForm;
import com.matthewnewkirk.kudos.forms.SearchKudoForm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
      User newUser = new User(
        0, registerUserForm.getUsername(), registerUserForm.getEmail());
      if (userService.findUserByEmail(registerUserForm.getEmail()) != null) {
        bindingResult.addError(
          new FieldError(
            "registerUserForm", "email", "A user already exists with this e-mail address."));
      }
      else {
        userService.add(newUser);
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
}