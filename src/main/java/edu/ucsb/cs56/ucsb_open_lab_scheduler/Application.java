package edu.ucsb.cs56.ucsb_open_lab_scheduler;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;


/*
class CustomAuthorizationRequestResolver implements OAuth2AuthorizationRequestResolver {

    private final OAuth2AuthorizationRequestResolver defaultResolver;

    public CustomAuthorizationRequestResolver(ClientRegistrationRepository repo,
            String authorizationRequestBaseUri) {
        defaultResolver = new DefaultOAuth2AuthorizationRequestResolver(repo, authorizationRequestBaseUri);
    }
    // ...

    @Override
    public OAuth2AuthorizationRequest resolve(final HttpServletRequest request) {
        OAuth2AuthorizationRequest req = defaultResolver.resolve(request);
        if (req != null) {
            req = customizeAuthorizationRequest(req);
        }
        return req;
    }

    @Override
    public OAuth2AuthorizationRequest resolve(final HttpServletRequest request, final String clientRegistrationId) {
        OAuth2AuthorizationRequest req = defaultResolver.resolve(request, clientRegistrationId);
        if (req != null) {
            req = customizeAuthorizationRequest(req);
        }
        return req;
    }

    private OAuth2AuthorizationRequest customizeAuthorizationRequest(final OAuth2AuthorizationRequest req) {
        // ...
        final Map<String, Object> extraParams = new HashMap<String, Object>();
        extraParams.putAll(req.getAdditionalParameters());
        extraParams.put("hd", "uscb.edu");

        return OAuth2AuthorizationRequest.from(req).additionalParameters(extraParams).build();
    }
}*/

@SpringBootApplication

public class Application extends WebSecurityConfigurerAdapter {

    public static void main(final String[] args) {
        SpringApplication.run(Application.class, args);
    }

    private static final String HOSTED_DOMAIN_ATTRIBUTE = "hd";

    @Value("#{'${allowed-domains}'.split(',')}")
    private List<String> allowedDomains;
    
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