package ru.ulmc.bank.server.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.ldap.core.ContextSource;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.support.BaseLdapPathContextSource;
import org.springframework.ldap.core.support.SimpleDirContextAuthenticationStrategy;
import org.springframework.security.authentication.encoding.LdapShaPasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.ldap.DefaultSpringSecurityContextSource;
import org.springframework.security.ldap.userdetails.LdapUserDetailsImpl;
import org.springframework.security.ldap.userdetails.LdapUserDetailsMapper;
import ru.ulmc.bank.core.service.impl.UserServiceImpl;
import ru.ulmc.bank.server.controller.AuthenticationController;

import java.util.Arrays;
import java.util.Collection;

/**
 * Настройки авторизации/аутентификации
 */
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${auth.provider}")
    private String authProvider;

    @Value("${auth.ldap.userDnPatterns}")
    private String userDnPatterns;

    @Value("${auth.ldap.groupSearchBase}")
    private String groupSearchBase;

    @Value("${auth.ldap.groupSearchFilter}")
    private String groupSearchFilter;

    @Value("${auth.ldap.passwordField}")
    private String userPasswordField;

    @Value("${auth.ldap.attribute.fullname}")
    private String fullnameAttribute;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth,
                                UserServiceImpl userService,
                                BaseLdapPathContextSource contextSource,
                                PasswordEncoder passwordEncoder) throws Exception {
        if (AuthProvider.LDAP.name().equalsIgnoreCase(authProvider)) {
            auth
                    .ldapAuthentication()
                    .userDnPatterns(userDnPatterns) //"uid={0},ou=people"
                    .groupSearchBase(groupSearchBase)//"ou=groups"
                    .contextSource(contextSource)
                    .rolePrefix("")
                    .userDetailsContextMapper(new LdapUserDetailsMapper() {
                        @Override
                        public UserDetails mapUserFromContext(DirContextOperations ctx, String username,
                                                              Collection<? extends GrantedAuthority> authorities) {
                            UserDetails user = super.mapUserFromContext(ctx, username, authorities);
                            userService.createOrUpdateUser((LdapUserDetailsImpl) user, ctx.getStringAttribute(fullnameAttribute));
                            return user;
                        }
                    })
                    .passwordCompare()
                    .passwordEncoder(new LdapShaPasswordEncoder())
                    .passwordAttribute(userPasswordField); //"userPassword"
        } else {
            auth
                    .userDetailsService(userService)
                    .passwordEncoder(passwordEncoder);
        }
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
