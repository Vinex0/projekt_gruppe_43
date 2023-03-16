package com.gruppe43.moneymanager.domain.dto;

import com.gruppe43.moneymanager.domain.Gruppe;
import java.util.ArrayList;
import java.util.List;

public class SchuldenToSchuldenDto {

  public static List<SchuldenDto> schuldenToSchuldenDto(Gruppe gruppe) {
    List<SchuldenDto> schuldenDtoList = new ArrayList<>();
    for (String domainSchuldner : gruppe.getSchulden().keySet()) {
      for (String domainGlaeubiger : gruppe.getGlaeubigerFrom(domainSchuldner).keySet()) {
        SchuldenDto schuldenDto = new SchuldenDto(domainGlaeubiger, domainSchuldner, gruppe.getSchuldenFromTo(domainSchuldner, domainGlaeubiger).getNumber().doubleValue());
        schuldenDtoList.add(schuldenDto);
      }
    }
    return schuldenDtoList;
  }
}
