package ru.ulmc.bank.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;


/**
 * Настройки авторизации/аутентификации
 */

//@EnableWebSecurity(debug = true)
@EnableGlobalAuthentication
//@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth, DataSource dataSource) throws Exception {
        //auth.inMemoryAuthentication().withUser("user").password("password").roles(Roles.ADMIN);
        auth.jdbcAuthentication().dataSource(dataSource);

        //todo: прикрутить БД
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
       /* http.authorizeRequests().antMatchers("*//*").permitAll();*/
        http.csrf().disable();
        http
                .authorizeRequests()
                .antMatchers("/vaadinServlet/**", "/VAADIN/**", "/auth/**", "/*").permitAll()
                .antMatchers("/auth*").anonymous()
                .anyRequest().authenticated()
                .and()
                .formLogin().disable()
                .logout().permitAll();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
