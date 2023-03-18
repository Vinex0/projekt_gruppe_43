package com.gruppe43.moneymanager.web;

import com.gruppe43.moneymanager.domain.Gruppe;
import com.gruppe43.moneymanager.github.GitUserValidation;
import com.gruppe43.moneymanager.helper.CheckboxHelper;
import com.gruppe43.moneymanager.service.GruppenService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.javamoney.moneta.Money;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes("nutzername")
public class MoneyManagerController {

  private final GruppenService gruppenService;

  public MoneyManagerController(GruppenService gruppenService) {
    this.gruppenService = gruppenService;
  }

  @GetMapping("/")
  public String startPage() {
    return "start";
  }

  @PostMapping("/")
  public String gruppenPage() {
    return "redirect:/gruppenOverview";
  }

  @GetMapping("/gruppenOverview")
  public String getGruppenPage(@AuthenticationPrincipal OAuth2User principal, Model model) {
    List<String> titles = new ArrayList<>(gruppenService.getTitles());
    String logIn = principal.getAttribute("login");
    model.addAttribute("nutzername", logIn);
    model.addAttribute("gruppen", titles);
    model.addAttribute("gruppenObject", gruppenService.getGruppenByNutzer(logIn));
    return "gruppenOverview";
  }


  @GetMapping("/createGruppe")
  public String createGruppe() {
    return "createGruppe";
  }

  @PostMapping("/createGruppe")
  public String submitGruppe(String name, @ModelAttribute("nutzername") String nutzername) {
    if (name.length() < 1) {
      return "redirect:createGruppe";
    }
    gruppenService.gruppeHinzufuegen(name, nutzername);

    return "redirect:gruppenOverview";
  }

  @GetMapping("/gruppe/{id}")
  public String getGruppe(@ModelAttribute("nutzername") String nutzername,
      @PathVariable("id") int id, Model model) {
    model.addAttribute("curNutzer", nutzername);
    model.addAttribute("gruppe", gruppenService.getGruppeById(id));
    return "gruppe";
  }

  @PostMapping("/addNutzer/{id}")
  public String addNutzer(@PathVariable("id") int id, String nutzername) {

    if (gruppenService.isClosed(id)) {
      return "redirect:/gruppe/" + id;
    }

    if (GitUserValidation.exists(nutzername)) {
      gruppenService.teilnehmerHinzufuegen(id, nutzername);
    }
    return "redirect:/gruppe/" + id;
  }

  @GetMapping("/createAusgabe/{id}")
  public String createAusgabe(@PathVariable("id") int id, Model model) {
    Gruppe gruppe = gruppenService.getGruppeById(id);
    ArrayList<CheckboxHelper> checkboxHelpers = gruppenService.getCheckboxHelper(id);

    model.addAttribute("checkboxHelpers", checkboxHelpers);
    model.addAttribute("gruppe", gruppe);
    return "createAusgabe";
  }

  @PostMapping("/createAusgabe/{id}")
  public String getAusgabe(@PathVariable("id") int id, String ausgabeTitel, String name,
      @RequestParam("summe") String summe, @RequestParam Map<String, String> allParams) {

    List<String> schuldenTeilnehmer = new ArrayList<>();

    allParams.remove("name");
    allParams.remove("_csrf");
    allParams.remove("summe");
    allParams.remove("ausgabeTitel");

    List<CheckboxHelper> checkboxHelpers = new ArrayList<>();
    for (Entry<String, String> e : allParams.entrySet()) {
      var a = new CheckboxHelper(e.getKey(), e.getValue().equals("on"));
      checkboxHelpers.add(a);
    }

    for (CheckboxHelper helper : checkboxHelpers) {
      if (helper.checked) {
        schuldenTeilnehmer.add(helper.person);
      }
    }
    if (!gruppenService.alleTeilnehmer(id).contains(name)) {
      return "redirect:/createAusgabe/" +id;
    }

    gruppenService.ausgabeHinzufuegen(id, name, schuldenTeilnehmer, Money.parse(summe + " EUR"),
        ausgabeTitel);
    return "redirect:/gruppe/" + id;
  }


  @PostMapping("/schliesseGruppe/{id}")
  public String closeGruppe(@PathVariable("id") int id) {
    gruppenService.close(id);
    return "redirect:/gruppe/" + id;
  }


}
