package com.matthewnewkirk.kudos;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import com.matthewnewkirk.kudos.containers.CompletedKudo;
import com.matthewnewkirk.kudos.containers.FoundKudos;
import com.matthewnewkirk.kudos.db.AddKudoService;
import com.matthewnewkirk.kudos.db.ReportingService;
import com.matthewnewkirk.kudos.db.UserService;
import com.matthewnewkirk.kudos.forms.AddKudoForm;
import com.matthewnewkirk.kudos.forms.SearchKudoForm;
import com.matthewnewkirk.kudos.util.UpdateUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * @author Matt Newkirk 11/21/2015
 */
@Controller
@SessionAttributes( types = { FoundKudos.class })
public class KudoController {
  @Autowired
  private ReportingService reportingService;

  @Autowired
  private AddKudoService addKudoService;

  @Autowired
  private UserService userService;

  @RequestMapping(value = "/")
  public String displayKudos(Model model,
                             @ModelAttribute @Valid SearchKudoForm searchKudoForm,
                             BindingResult bindingResult) {
    if (!model.containsAttribute("addKudoForm")) {
      AddKudoForm addKudoForm = new AddKudoForm();
      UpdateUtil.updateAvailableUsersList(addKudoForm, userService);
      model.addAttribute("addKudoForm", addKudoForm);
    }
    else {
      AddKudoForm addKudoForm = (AddKudoForm)model.asMap().get("addKudoForm");
      UpdateUtil.updateAvailableUsersList(addKudoForm, userService);
    }
    if (bindingResult.hasErrors()) {
      model.addAttribute(new FoundKudos(new ArrayList<CompletedKudo>(), searchKudoForm));
    }
    else {
      refreshKudos(model, searchKudoForm);
    }
    model.addAttribute("searchKudoForm", searchKudoForm);
    return "home";
  }

  @RequestMapping(method = RequestMethod.POST, value = "/add")
  public String addKudoAndReturnResults(Model model,
                                        @ModelAttribute @Valid AddKudoForm addKudoForm,
                                        BindingResult bindingResult,
                                        RedirectAttributes attr) {
    addKudoForm.validate(bindingResult);
    model.addAttribute("addKudoForm", addKudoForm);
    if (!bindingResult.hasErrors()) {
      addKudoService.addKudo(addKudoForm);
    }
    else {
      attr.addFlashAttribute("org.springframework.validation.BindingResult.addKudoForm", bindingResult);
    }
    attr.addFlashAttribute("addKudoForm", addKudoForm);

    return "redirect:/";
  }

  private void refreshKudos(Model model, SearchKudoForm searchKudoForm) {
    List<CompletedKudo> kudos = new ArrayList<>();
    kudos =
      reportingService.findLastNKudos(Integer.valueOf(searchKudoForm.getLimitNumberOfKudosDisplayed()));
    FoundKudos foundKudos = new FoundKudos(kudos, searchKudoForm);
    model.addAttribute(foundKudos);
  }
}
