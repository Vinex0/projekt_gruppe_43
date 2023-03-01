package com.gruppe43.moneymanager.securityconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

  @Bean
  public SecurityFilterChain configure(HttpSecurity chainBuilder) throws Exception {

    chainBuilder.authorizeHttpRequests(
            configurer -> configurer
                .requestMatchers("/", "/css/*").permitAll()
                .anyRequest().authenticated()
        ).httpBasic(Customizer.withDefaults())
        .oauth2Login(config ->
            config.userInfoEndpoint(
                info -> info.userService(new AppUserService())
            ));

    return chainBuilder.build();
  }


}
