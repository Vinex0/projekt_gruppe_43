package com.gruppe43.moneymanager.web;

import com.gruppe43.moneymanager.domain.Gruppe;
import com.gruppe43.moneymanager.service.CheckboxHelper;
import com.gruppe43.moneymanager.service.GruppenService;
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import org.javamoney.moneta.Money;
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
  public String add(Model model, String nutzername) {
    //TODO nutzername
    model.addAttribute("nutzername", "Peter");
    return "redirect:/gruppenOverview";
  }

  @GetMapping("/gruppenOverview")
  public String getGroupPage(@ModelAttribute("nutzername") String nutzername, Model model) {
    List<String> titles = new ArrayList<>(gruppenService.getTitles());
    model.addAttribute("gruppen", titles);
    return "gruppenOverview";
  }

  @GetMapping("/group")
  public String getGroupInfo(@RequestParam("gruppenName") String gruppenName, Model model) {
    return "gruppe";
  }

  @GetMapping("/createGruppe")
  public String createGruppe(Model model) {
    return "createGruppe";
  }

  @PostMapping("/createGruppe")
  public String submitGruppe(String name, @ModelAttribute("nutzername") String nutzername) {
    if (name.length() < 1) {
      return "redirect:createGruppe";
    }
    gruppenService.addGruppe(name, nutzername);
    return "redirect:gruppenOverview";
  }

  @GetMapping("/gruppe/{title}")
  public String getGruppe(@PathVariable("title") String title, Model model) {
    model.addAttribute("gruppe", gruppenService.getGruppe(title));
    return "gruppe";
  }

  @PostMapping("/addNutzer/{title}")
  public String addNutzer(@PathVariable("title") String title, String nutzername) {
    Gruppe gruppe = gruppenService.getGruppe(title);
    gruppe.addTeilnehmer(nutzername);
    return "redirect:/gruppe/" + title;
  }

  @GetMapping("/createAusgabe/{title}")
  public String createAusgabe(@PathVariable("title") String title, Model model) {
    Gruppe gruppe = gruppenService.getGruppe(title);
    ArrayList<CheckboxHelper> checkboxHelpers = gruppenService.getCheckboxHelper(title);

    model.addAttribute("checkboxHelpers", checkboxHelpers);
    model.addAttribute("gruppe", gruppe);
    return "createAusgabe";
  }

  @PostMapping("/createAusgabe/{title}")
  public String getAusgabe(Model model,
      @PathVariable("title") String title,
      String ausgabeTitel,
      String name,
      @RequestParam("summe") String summe,
      @RequestParam Map<String, String> allParams) {

    Gruppe gruppe = gruppenService.getGruppe(title);
    List<String> schuldenTeilnehmer = new ArrayList<>();

    allParams.remove("name");
    allParams.remove("_csrf");
    allParams.remove("summe");
    allParams.remove("ausgabeTitel");

    List<CheckboxHelper> checkboxHelpers = new ArrayList<>();
    for (Entry<String, String> e : allParams.entrySet()) {

      //TODO Check if correct refactored  var a = new CheckboxHelper(personService.getPerson(e.getKey()), e.getValue().equals("on"));
      var a = new CheckboxHelper(e.getKey(), e.getValue().equals("on"));
      checkboxHelpers.add(a);
    }

    for (CheckboxHelper helper : checkboxHelpers) {
      if (helper.checked) {
        schuldenTeilnehmer.add(helper.person);
      }
    }

    gruppe.createAusgabe(name, schuldenTeilnehmer,
        Money.parse(summe + " EUR"), ausgabeTitel);
    return "redirect:/gruppe/" + title;
  }

}
