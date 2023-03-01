package com.gruppe43.moneymanager.web;

import com.gruppe43.moneymanager.domain.GroupActionController;
import com.gruppe43.moneymanager.domain.Person;
import com.gruppe43.moneymanager.service.PersonRepository;
import com.gruppe43.moneymanager.service.PersonService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class MoneyManagerController {

  private final PersonService personService;

  public MoneyManagerController(PersonService service) {
    this.personService = service;
  }

  @GetMapping("/")
  public String startPage() {
    return "start";
  }

  @PostMapping("/")
  public String add(Model model) {

    Person person = personService.getPerson("Peter");
    model.addAttribute("username", person.getUserName());
    return "redirect:/groupPage";
  }

  @GetMapping("/groupPage")
  public String getGroupPage() {
    return "groupPage";
  }

}
