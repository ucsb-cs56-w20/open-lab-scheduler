package edu.ucsb.cs56.ucsb_open_lab_scheduler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;



@SpringBootApplication

public class Application extends WebSecurityConfigurerAdapter {

    public static void main(final String[] args) {
        SpringApplication.run(Application.class, args);
    }

    private static final String HOSTED_DOMAIN_ATTRIBUTE = "hd";
    
    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http
        .authorizeRequests()
            .antMatchers("/","/login**","/webjars/**","/error**")
            .permitAll()
        .anyRequest()
            .authenticated()
        .and()
            .oauth2Login().loginPage("/login")
        .and()
            .logout()
            .deleteCookies("remove")
            .invalidateHttpSession(true)
            .logoutUrl("/logout")
            .logoutSuccessUrl("/")
            .permitAll();
    }
}