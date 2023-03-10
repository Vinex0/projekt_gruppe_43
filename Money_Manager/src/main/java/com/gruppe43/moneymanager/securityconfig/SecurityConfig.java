package com.gruppe43.moneymanager.securityconfig;

import java.util.Arrays;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class SecurityConfig {

  @Bean
  public SecurityFilterChain configure(HttpSecurity chainBuilder) throws Exception {

    chainBuilder.cors().and().csrf().disable();

    chainBuilder.authorizeHttpRequests(
        configurer -> configurer
            .requestMatchers("*", "/", "/css/*").permitAll().requestMatchers(HttpMethod.POST, "*", "/schliesseGruppe/*","/api/*").permitAll()
                .requestMatchers(HttpMethod.GET,"/api/*","/").permitAll()
                .anyRequest().authenticated()
    ).oauth2Login();

    return chainBuilder.build();
  }

}
