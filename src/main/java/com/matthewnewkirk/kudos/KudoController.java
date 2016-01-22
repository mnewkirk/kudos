package com.matthewnewkirk.kudos;

import java.util.Date;
import java.util.List;

import com.matthewnewkirk.kudos.containers.CompletedKudo;
import com.matthewnewkirk.kudos.containers.Kudo;
import com.matthewnewkirk.kudos.containers.search.FoundKudos;
import com.matthewnewkirk.kudos.containers.search.KudoSearch;
import com.matthewnewkirk.kudos.containers.search.KudoToAdd;
import com.matthewnewkirk.kudos.db.AddKudoService;
import com.matthewnewkirk.kudos.db.KudoService;
import com.matthewnewkirk.kudos.db.ReportingService;
import com.matthewnewkirk.kudos.db.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 * @author Matt Newkirk 11/21/2015
 */
@Controller
@SessionAttributes(types = { FoundKudos.class, })
public class KudoController {
  @Autowired
  private ReportingService reportingService;

  @Autowired
  private AddKudoService addKudoService;

  @RequestMapping(value = "/")
  public String listKudos(Model model, @ModelAttribute KudoSearch kudoSearch) {
    List<CompletedKudo> kudos =
      reportingService.findLastNKudos(kudoSearch.getLimitNumberOfKudosDisplayed());
    FoundKudos foundKudos = new FoundKudos(kudos, kudoSearch);
    model.addAttribute(foundKudos);
    model.addAttribute(new KudoToAdd());
    return "list";
  }
  @RequestMapping(method = RequestMethod.POST, value = "/search")
  public String searchForKudos(Model model, @ModelAttribute KudoSearch kudoSearch) {
    return listKudos(model, kudoSearch);
  }

  @RequestMapping(method = RequestMethod.POST, value = "/add")
  public String addKudoAndReturnResults(Model model,
                                        @ModelAttribute KudoSearch kudoSearch,
                                        @ModelAttribute KudoToAdd kudoToAdd) {
    addKudoService.addKudo(kudoToAdd);
    return listKudos(model, kudoSearch);
  }
}
