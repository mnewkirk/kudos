package com.matthewnewkirk.kudos;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import com.matthewnewkirk.kudos.containers.CompletedKudo;
import com.matthewnewkirk.kudos.containers.FoundKudos;
import com.matthewnewkirk.kudos.containers.Kudo;
import com.matthewnewkirk.kudos.forms.SearchKudoForm;
import com.matthewnewkirk.kudos.forms.AddKudoForm;
import com.matthewnewkirk.kudos.db.AddKudoService;
import com.matthewnewkirk.kudos.db.ReportingService;

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
@SessionAttributes( types = { FoundKudos.class})
public class KudoController {
  @Autowired
  private ReportingService reportingService;

  @Autowired
  private AddKudoService addKudoService;

  @RequestMapping(value = "/")
  public String searchForKudos(Model model, @ModelAttribute @Valid SearchKudoForm searchKudoForm,
                               BindingResult bindingResult) {
    if (!model.containsAttribute("addKudoForm")) {
      model.addAttribute(new AddKudoForm());
    }
    if (bindingResult.hasErrors()) {
      model.addAttribute(new FoundKudos(new ArrayList<CompletedKudo>(), searchKudoForm));
    }
    else {
      refreshKudos(model, searchKudoForm);
    }
    model.addAttribute(searchKudoForm);
    return "list";
  }

  @RequestMapping(method = RequestMethod.POST, value = "/add")
  public String addKudoAndReturnResults(Model model,
                                        @ModelAttribute @Valid AddKudoForm addKudoForm,
                                        BindingResult bindingResult,
                                        RedirectAttributes attr) {
    addKudoForm.validate(bindingResult);
    SearchKudoForm searchKudoForm =
      new SearchKudoForm(((FoundKudos)model.asMap().get("foundKudos")).getSearchSize());
    model.addAttribute(addKudoForm);
    model.addAttribute(searchKudoForm);
    if (!bindingResult.hasErrors()) {
      addKudoService.addKudo(addKudoForm);
      //refreshKudos(model, searchKudoForm);
    }
    else {
      attr.addFlashAttribute("org.springframework.validation.BindingResult.addKudoForm", bindingResult);
    }
    attr.addFlashAttribute(searchKudoForm);
    attr.addFlashAttribute(addKudoForm);
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
