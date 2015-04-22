package db.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Created by StudentDB on 22.04.2015.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser("admin").password("123456").roles("ADMIN", "DBA");
        auth.inMemoryAuthentication().withUser("user").password("123456").roles("USER");
        auth.inMemoryAuthentication().withUser("dba").password("123456").roles("DBA");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
                .antMatchers("/book/update**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_DBA')")
                .antMatchers("/book/create**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_DBA')")
                .antMatchers("/book/removeAuthority").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_DBA')")
                .antMatchers("/author/update").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_DBA')")
                .antMatchers("/author/create").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_DBA')")
                .antMatchers("/**").access("hasRole('ROLE_USER') or hasRole('ROLE_DBA') or hasRole('ROLE_ADMIN')")
                .and().formLogin()
                .and().logout().logoutUrl("/logout");

    }
}
