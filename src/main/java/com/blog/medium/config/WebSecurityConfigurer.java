package com.blog.medium.config;

import com.blog.medium.filter.JwtRequestFilter;
import com.blog.medium.service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {

    @Autowired
    MyUserDetailsService myUserDetailsService;

    @Autowired
    JwtRequestFilter jwtRequestFilter;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(myUserDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/v2/api-docs",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui.html",
                "/webjars/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
                http.csrf().disable()
                .authorizeRequests()
                    .antMatchers("/swagger-ui.html").permitAll()
                    .antMatchers("/api/admin").hasRole("ADMIN")
                    .antMatchers(HttpMethod.GET,"/api/posts").permitAll()
                    .antMatchers("/api/posts/{id}").permitAll()
                    .antMatchers(HttpMethod.GET,"/authenticate").permitAll()
                    .antMatchers(HttpMethod.POST,"/api/users").permitAll()
                    .antMatchers(HttpMethod.POST,"/authenticate").permitAll()
                    .antMatchers(HttpMethod.POST,"/api/posts").authenticated()
                    .antMatchers(HttpMethod.PUT,"/api/posts").authenticated()
                    .antMatchers(HttpMethod.PATCH,"/api/posts").authenticated()
                    .antMatchers(HttpMethod.DELETE,"/api/posts").authenticated()
                        .anyRequest()
                    .authenticated()
                    .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder () {
        return new BCryptPasswordEncoder();
    }


}
