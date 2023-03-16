package com.gruppe43.moneymanager.domain.dto;

import com.gruppe43.moneymanager.domain.Gruppe;
import com.gruppe43.moneymanager.service.GruppenService;
import java.util.ArrayList;
import java.util.List;

public class GruppenToSchuldenDto {
  public static List<List<SchuldenDto>> gruppenToSchuldenDto(GruppenService gruppenService) {
    List<List<SchuldenDto>> schuldenListe = new ArrayList<>();
    for (Gruppe gruppe : gruppenService.alleGruppen()) {
      schuldenListe.add(SchuldenToSchuldenDto.schuldenToSchuldenDto(gruppe));
    }
    return schuldenListe;
  }
}
