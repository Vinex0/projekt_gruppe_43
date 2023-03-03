package com.gruppe43.moneymanager.web;

import com.gruppe43.moneymanager.domain.Gruppe;
import com.gruppe43.moneymanager.domain.Person;
import com.gruppe43.moneymanager.service.GruppenService;
import com.gruppe43.moneymanager.service.PersonService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes("username")
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
    List<String> titles = p.getGruppen().stream().map(Gruppe::title).collect(Collectors.toList());
    titles.add("TestGruppe1");
    titles.add("TestGruppe2");

    model.addAttribute("gruppen", titles);

    return "gruppenOverview";
  }


  @GetMapping("/group")
  public String getGroupInfo(@RequestParam("gruppenName") String gruppenName, Model model) {
    Gruppe gruppe = gruppenService.getGruppe(gruppenName);
    model.addAttribute("grup", gruppe);
    return "gruppe";
  }
     /*
    @PostMapping("/createGroup")
    public String createGroup(@ModelAttribute("username") Person username, String title){
        GruppenActionController groupActionController = groupService.createGroup(title, username);

        return "redirect:/groupPage";
    }
    //TODO
    @PostMapping("/addPerson")
    public String addPerson(@ModelAttribute("username") Person username, @ModelAttribute("groupTitle") String title){


        return "redirect:/groupPage";
    }

    */

}
