package com.gruppe43.moneymanager.web;

import com.gruppe43.moneymanager.domain.Person;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class MoneyManagerController {


  Person person;
  @GetMapping("/")
  public String startPage(){
    return "start";
  }

  @PostMapping("/")
  public String add(OAuth2AuthenticationToken authenticationToken, RedirectAttributes redirectAttributes) {
    person.setUserName(authenticationToken.getPrincipal().getName());

    return null;
  }

}
