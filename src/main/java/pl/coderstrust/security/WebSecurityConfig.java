package pl.coderstrust.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
            //.antMatchers("/invoices/**").hasRole("USER")//permitAll()
            .antMatchers("/invoices/**").hasRole("USER")
            .and()
            .httpBasic();
            /*.anyRequest().fullyAuthenticated()
            .and()
            .formLogin()
            .loginPage("/login")
            .permitAll()
            .and()
            .logout()
            .permitAll();*/
    }
/*
    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        UserDetails user =
            User.withDefaultPasswordEncoder()
                .username("admin")  // niewazne co tu wpisze testy przechodza
                .password("admin")   // niewazne co tu wpisze testy przechodza
                .roles("USER")  // niewazne co tu wpisze testy przechodza
                .build();
        return new InMemoryUserDetailsManager(user);
    }*/

   /* @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .inMemoryAuthentication()
            .withUser("admin").password("admin").roles("USER");
    }*/
}
