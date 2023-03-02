package com.gruppe43.moneymanager.web;

import com.gruppe43.moneymanager.domain.Group;
import com.gruppe43.moneymanager.domain.GroupActionController;
import com.gruppe43.moneymanager.domain.Person;
import com.gruppe43.moneymanager.service.GroupService;
import com.gruppe43.moneymanager.service.PersonService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@SessionAttributes("username")
public class MoneyManagerController {

    private final PersonService personService;
    private final GroupService groupService;

    public MoneyManagerController(PersonService service, GroupService groupService, GroupService groupService1) {
        this.personService = service;
        this.groupService = groupService1;
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

    @GetMapping("/groupOverview")
    public String getGroupPage(@ModelAttribute("username") String username, Model model) {
        Person p = personService.getPerson(username);
        List<String> titles = p.getGroups().stream().map(Group::title).collect(Collectors.toList());
        titles.add("TestGruppe1");
        titles.add("TestGruppe2");

        model.addAttribute("groups", titles);

        return "groupPage";
    }

    /*
    @GetMapping("/group")
    public String getGroupInfo(@RequestParam("groupName") String groupName, Model model) {
        return "groupPage";
    }

    @PostMapping("/createGroup")
    public String createGroup(@ModelAttribute("username") Person username, String title){
        GroupActionController groupActionController = groupService.createGroup(title, username);

        return "redirect:/groupPage";
    }
    //TODO
    @PostMapping("/addPerson")
    public String addPerson(@ModelAttribute("username") Person username, @ModelAttribute("groupTitle") String title){


        return "redirect:/groupPage";
    }

    */

}
