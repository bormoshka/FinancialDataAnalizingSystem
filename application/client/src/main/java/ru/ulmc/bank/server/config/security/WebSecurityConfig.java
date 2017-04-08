package ru.ulmc.bank.server.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;


/**
 * Настройки авторизации/аутентификации
 */
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth,
                                UserDetailsService userService,
                                PasswordEncoder passwordEncoder) throws Exception {
        auth
                // .authenticationProvider(new DaoAuthenticationProvider())
                .userDetailsService(userService)
                .passwordEncoder(passwordEncoder);

    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http
                .authorizeRequests()
                .antMatchers("/vaadinServlet/**", "/VAADIN/**", "/auth/**", "/*").permitAll()
                .antMatchers("/").anonymous()
                .anyRequest().authenticated()
                .and()
                .formLogin().disable()
                .logout().permitAll();
    }

}
