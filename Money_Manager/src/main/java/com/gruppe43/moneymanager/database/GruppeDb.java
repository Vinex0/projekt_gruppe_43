package com.gruppe43.moneymanager.database;

import java.util.List;
import org.springframework.data.annotation.Id;

public record GruppeDb(@Id String id, String titel, String startPerson, Boolean geschlossen) {

}
