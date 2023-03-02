package com.gruppe43.moneymanager.web;

import com.gruppe43.moneymanager.domain.Group;
import com.gruppe43.moneymanager.domain.GroupActionController;
import com.gruppe43.moneymanager.domain.Person;
import com.gruppe43.moneymanager.service.PersonRepository;
import com.gruppe43.moneymanager.service.PersonService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@SessionAttributes("username")
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
    public String getGroupPage(@ModelAttribute("username") String username, Model model) {
        Person p = personService.getPerson(username);
        List<String> titles = p.getGroups().stream().map(Group::title).collect(Collectors.toList());
        titles.add("TestGruppe1");
        titles.add("TestGruppe2");

        model.addAttribute("groups", titles);

        return "groupPage";
    }

}
