package com.udacity.jwdnd.course1.cloudstorage.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final AuthenticationService authenticationService;

    public SecurityConfig(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        //http
                //.authorizeRequests()
                //.anyRequest().permitAll() // Permit all requests
                //.and()

                http
                        .authorizeRequests()
                        .antMatchers("/signup", "/css/**", "/js/**").permitAll() // Allow access to signup and static resources
                        .anyRequest().authenticated() // Require authentication for any other requests
                        .and()
                        .formLogin()
                        .loginPage("/login") // Custom login page URL
                        .successHandler(new SimpleUrlAuthenticationSuccessHandler("/home"))
                        .permitAll() // Allow everyone to access the login page
                        .and()
                        .logout()
                        .permitAll(); // Allow everyone to logout

                http.csrf().disable(); // Disable CSRF protection
            }
        }



