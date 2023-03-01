package com.gruppe43;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MoneyManagerController {

  @GetMapping("/")
  public String startPage(){
    return "start";
  }


}
