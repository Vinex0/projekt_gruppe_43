package com.gruppe43.moneymanager.domain.dto;

import com.gruppe43.moneymanager.stereotypes.Wertobjekt;
import java.util.List;
import org.javamoney.moneta.Money;


@Wertobjekt
public record AusgabeDto(String glaeubiger, List<String> schuldnerListe, String titel,
                         Money summe) {

}

