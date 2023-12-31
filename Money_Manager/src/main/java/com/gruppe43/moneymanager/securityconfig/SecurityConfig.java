package com.gruppe43.moneymanager.securityconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

  @Bean
  public SecurityFilterChain configure(HttpSecurity chainBuilder) throws Exception {

    chainBuilder.cors().and().csrf()
        .ignoringRequestMatchers("/api/gruppen", "/api/gruppen/*", "/api/gruppen/*/schliessen",
            "/api/gruppen/*/auslagen");

    chainBuilder.authorizeHttpRequests(
        configurer -> configurer
            .requestMatchers("*", "/", "/css/*", "/signin.css").permitAll()
            .requestMatchers(HttpMethod.POST, "*", "/schliesseGruppe/*", "/api/gruppen",
                "/api/gruppen/*", "/api/gruppen/*/schliessen", "/api/gruppen/*/auslagen")
            .permitAll()
            .requestMatchers(HttpMethod.GET, "/", "/api/user/*/gruppen", "/api/gruppen/*",
                "/api/gruppen/*/ausgleich").permitAll()
            .anyRequest().authenticated()
    ).oauth2Login();

    return chainBuilder.build();
  }

}
