package com.gruppe43.moneymanager.web;

import com.gruppe43.moneymanager.domain.Ausgabe;
import com.gruppe43.moneymanager.domain.Gruppe;
import com.gruppe43.moneymanager.domain.Person;
import com.gruppe43.moneymanager.service.GruppenService;
import com.gruppe43.moneymanager.service.PersonService;
import java.util.ArrayList;
import java.util.HashMap;
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

  private final PersonService personService;
  private final GruppenService gruppenService;

  public MoneyManagerController(PersonService service, GruppenService gruppenService) {
    this.personService = service;
    this.gruppenService = gruppenService;
  }

  @GetMapping("/")
  public String startPage() {
    return "start";
  }

  @PostMapping("/")
  public String add(Model model) {
    Person person = personService.getPerson("Peter");
    model.addAttribute("nutzername", person.getNutzerName());
    return "redirect:/gruppenOverview";
  }

  @GetMapping("/gruppenOverview")
  public String getGroupPage(@ModelAttribute("nutzername") String nutzername, Model model) {

    Person p = personService.getPerson(nutzername);
    List<String> titles = p.getGruppen().stream().map(Gruppe::getTitel).collect(Collectors.toList());
    titles.add("TestGruppe1");
    titles.add("TestGruppe2");
    titles.addAll(gruppenService.getTitles());
    model.addAttribute("gruppen", titles);

    return "gruppenOverview";
  }

  @GetMapping("/group")
  public String getGroupInfo(@RequestParam("gruppenName") String gruppenName, Model model) {
   // Gruppe gruppe = gruppenService.getGruppe(gruppenName);
    //model.addAttribute("grup", gruppe);
    return "gruppe";
  }

  @GetMapping("/createGruppe")
  public String createGruppe(Model model) {
    return "createGruppe";
  }

  @PostMapping("/createGruppe")
  public String submitGruppe(String name, @ModelAttribute("nutzername") String nutzername) {
    if(name.length() < 1) return "redirect:createGruppe";
    gruppenService.addGruppe(name, personService.getPerson(nutzername));
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
    gruppe.addTeilnehmer(personService.getPerson(nutzername));
    return "redirect:/gruppe/" + title;
  }

  @GetMapping("/createAusgabe/{title}")
  public String createAusgabe(@PathVariable("title") String title, Model model) {
    Gruppe gruppe = gruppenService.getGruppe(title);
    Map<Person, Boolean> toggleHelper = gruppenService.getToggleHelper(title);

    model.addAttribute("toggleHelper", toggleHelper);
    model.addAttribute("gruppe", gruppe);
    return "createAusgabe";
  }

  @PostMapping("/createAusgabe/{title}")
  public String getAusgabe(@PathVariable("title") String title, String ausgabeTitel, String name, String summe) {
    Gruppe gruppe = gruppenService.getGruppe(title);
    Map<Person, Boolean> toggleHelper = gruppenService.getToggleHelper(title);
    List<Person> schuldenTeilnehmer = new ArrayList<>();
    for (Entry<Person, Boolean> e : toggleHelper.entrySet()) {
      if(e.getValue().equals(true)) {
        schuldenTeilnehmer.add(e.getKey());
      }
    }

    gruppe.createAusgabe(personService.getPerson(name), schuldenTeilnehmer, Money.parse(summe + "EUR"), ausgabeTitel);
    return "redirect:/gruppe/" + title;
  }

}
