package se.callista.blog.pki.sampleserver;

import java.security.Principal;
import java.util.Collections;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableWebSecurity
@RestController
public class SampleServerApplication {

  public static void main(String[] args) {
    SpringApplication.run(SampleServerApplication.class, args);
  }

  @GetMapping("/")
  public String index(Principal principal) {
    String user = "anonymous";
    if (principal != null) {
      UserDetails currentUser
          = (UserDetails) ((Authentication) principal).getPrincipal();
      if (currentUser != null) {
        user = currentUser.getUsername();
      }
    }
    return "hello, " + user;
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.anonymous().and()
        .authorizeRequests()
        .anyRequest()
        .permitAll()
        .and()
        .x509()
        .subjectPrincipalRegex("CN=(.*?)(?:,|$)")
        .userDetailsService(userDetailsService());
    return http.build();
  }

  @Bean
  public UserDetailsService userDetailsService() {
    return new UserDetailsService() {
      @Override
      public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new User(username, "", Collections.emptyList());
      }
    };
  }
}
