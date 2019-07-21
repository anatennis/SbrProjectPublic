package ru.sberbank.javaschool.edu.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.sberbank.javaschool.edu.service.UserService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder(5);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authenticationProvider(new AuthenticationProvider() {
                    @Override
                    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
                        return null;
                    }

                    @Override
                    public boolean supports(Class<?> authentication) {
                        return false;
                    }
                })
                .authorizeRequests()
                .antMatchers("/", "/registration", "/activate/*").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                //.addFilter()
                .formLogin()
                .loginPage("/login")

                .defaultSuccessUrl("/user")
                .permitAll()
                .and()
                .rememberMe()
                .and()
                .logout()
                .permitAll();

    }

    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService)
                .passwordEncoder(passwordEncoder)


        ;

    }

}