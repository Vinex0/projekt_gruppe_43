package com.gruppe43.moneymanager.web;

import com.gruppe43.moneymanager.domain.Gruppe;
import com.gruppe43.moneymanager.service.GruppenService;
import com.gruppe43.moneymanager.service.SerializerService;
import java.util.ArrayList;
import java.util.List;
import org.javamoney.moneta.Money;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ApiController {

  private final GruppenService gruppenService;

  public ApiController(GruppenService gruppenService) {
    this.gruppenService = gruppenService;
  }

  @GetMapping("/api/gruppen/{id}")
  public ResponseEntity<?> getGruppe(@PathVariable("id") String id) {
    Gruppe gruppe = gruppenService.getGruppeById(Integer.parseInt(id));
    if (gruppe == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    return ResponseEntity.status(HttpStatus.OK).body(SerializerService.gruppeToJson(gruppe));
  }

  @GetMapping("/api/gruppen/{id}/ausgleich")
  public ResponseEntity<?> getSchulden(@PathVariable("id") String id) {
    if (gruppenService.getGruppeById(Integer.parseInt(id)) == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    return ResponseEntity.status(HttpStatus.OK)
        .body(SerializerService.schuldenToJson(gruppenService.getGruppeById(Integer.parseInt(id))));
  }

  @GetMapping("/api/user/{name}/gruppen")
  public String getGruppenOfPerson(@PathVariable("name") String name) {
    return SerializerService.gruppenByNutzerToJson(gruppenService.getGruppenByNutzer(name));
  }

  @PostMapping("/api/gruppen")
  public ResponseEntity<?> createGruppe(@RequestBody String data) throws JSONException {

    try {
      JSONObject obj = new JSONObject(data);
      String name = obj.getString("name");
      JSONArray personen = obj.getJSONArray("personen");
      if (personen.length() < 1) {
        throw new JSONException("test");
      }
      List<String> pers = new ArrayList<>();
      for (int i = 0; i < personen.length(); i++) {
        pers.add(personen.getString(i));
      }
      //TODO check this
      gruppenService.gruppeHinzufuegen(name, pers.get(0));
      Gruppe g = gruppenService.getGruppeByTitle(name);
      pers.remove(0);
      for (String p : pers) {
        //g.addTeilnehmer(p);
        gruppenService.teilnehmerHinzufuegen(g.getId(), p);
      }
      return ResponseEntity.status(HttpStatus.CREATED)
          .body(gruppenService.getGruppeByTitle(name).getId());
    } catch (JSONException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }


  }

  @PostMapping("/api/gruppen/{id}/schliessen")
  public ResponseEntity<?> closeGruppe(@PathVariable("id") String id) {
    if (gruppenService.getGruppeById(Integer.parseInt(id)) != null) {
      gruppenService.close(Integer.parseInt(id));
      return ResponseEntity.status(HttpStatus.OK).build();
    }
    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
  }

  @PostMapping("/api/gruppen/{id}/auslagen")
  public ResponseEntity<?> createAusgabe(@PathVariable String id, @RequestBody String data) {

    try {
      JSONObject obj = new JSONObject(data);

      JSONArray schuldner = obj.getJSONArray("schuldner");
      if (schuldner.length() < 1) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
      }
      List<String> schuld = new ArrayList<>();

      for (int i = 0; i < schuldner.length(); i++) {
        schuld.add(schuldner.getString(i));
      }
      String grund = obj.getString("grund");
      String glaeubiger = obj.getString("glaeubiger");
      int summe = obj.getInt("cent");

      if (gruppenService.getGruppeById(Integer.parseInt(id)) == null) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
      }
      if (gruppenService.isClosed(Integer.parseInt(id))) {
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
      }
      gruppenService.ausgabeHinzufuegen(Integer.parseInt(id), glaeubiger, schuld, Money.of(summe, "EUR").divide(100),
          grund);
      return ResponseEntity.status(HttpStatus.CREATED).build();
    } catch (JSONException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

  }

}
