package com.gruppe43.moneymanager.github;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class GitUserTester {
  public static boolean exists(String username) {
    HttpClient client = HttpClient.newHttpClient();
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create("https://api.github.com/users/" + username))
        .GET()
        .build();
    try {
      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
      if (response.toString().contains("200")) {
        return true;
      }
    } catch (InterruptedException | IOException e) {
      e.printStackTrace();
    }
    return false;
  }
}
