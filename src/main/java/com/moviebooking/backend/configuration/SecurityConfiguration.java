package com.moviebooking.backend.configuration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter{
	
	private static String REALM="MY_TEST_REALM";

//    @Autowired
//    @Qualifier("dataSource")
//    private DataSource dataSource;
    
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    	 auth.inMemoryAuthentication().withUser("avi").password(passwordEncoder().encode("123")).roles("ADMIN");
         auth.inMemoryAuthentication().withUser("user").password(passwordEncoder().encode("user")).roles("USER");
    	
//        auth.jdbcAuthentication().dataSource(dataSource)
//            .authoritiesByUsernameQuery("select user_name, role_id FROM movie.user_details where user_name=?")
//            .usersByUsernameQuery("select user_name,password as password,1 FROM movie.user_details where user_name=?");
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	http                    // it indicate basic authentication is requires
        .authorizeRequests()
        .antMatchers( "/index").permitAll() // /index will be accessible directly, no need of any authentication
        .anyRequest().authenticated()
        .and()
        .httpBasic()  ;    // it's indicate all request will be secure
http.csrf().disable();
    	
//        http.csrf().disable()
//        .authorizeRequests()
//        .antMatchers( "/index").permitAll()
//        .and().httpBasic().realmName(REALM).authenticationEntryPoint(getBasicAuthEntryPoint())
//        .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        
    }
	
    @Bean
    public PasswordEncoder passwordEncoder() {
    	return new BCryptPasswordEncoder();
    }
}
