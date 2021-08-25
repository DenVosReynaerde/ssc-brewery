package guru.sfg.brewery.configuration;

import guru.sfg.brewery.security.FoxtrotPasswordEncoderFactories;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class securityConfig extends WebSecurityConfigurerAdapter {

    /**
     * Password encoder factories is used to support different encoding types. this for lagacy purposes.
     * @return
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return FoxtrotPasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable();

        http.authorizeRequests(authorize -> {
            authorize
                    .antMatchers("/h2-console/**").permitAll() //enable H2-console, do not use in production
                    .antMatchers("/webjars/**", "/login", "/resources/**").permitAll()
                    .antMatchers("/").permitAll()
                    .antMatchers(HttpMethod.GET, "/api/v1/beer/").permitAll()
                    .mvcMatchers(HttpMethod.GET, "/brewery/api/v1/breweries")
                        .hasAnyRole("ADMIN", "CUSTOMER")
                    .mvcMatchers(HttpMethod.DELETE, "/api/v1/beer/**").hasRole("ADMIN")
                    .mvcMatchers("/brewery/breweries").hasAnyRole("ADMIN", "CUSTOMER")
                    .mvcMatchers(HttpMethod.GET, "/beers/**").hasAnyRole("ADMIN", "CUSTOMER", "USER")
                    .mvcMatchers(HttpMethod.GET, "/api/v1/beerUpc/{upc}")
                        .hasAnyRole("ADMIN", "CUSTOMER", "USER");
        })
        .authorizeRequests()
        .anyRequest().authenticated()
        .and()
        .formLogin()
        .and()
        .httpBasic();

        http.headers().frameOptions().sameOrigin(); //enable frames, makes the H2 console work correctly.
    }

/*    @Override
    @Bean
    protected UserDetailsService userDetailsService() {
        UserDetails admin = User.withDefaultPasswordEncoder()
                .username("foxtrot")
                .password("secret")
                .roles("ADMIN")
                .build();

        UserDetails user = User.withDefaultPasswordEncoder()
                .username("user")
                .password("password")
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(admin, user);
    }*/


    /**
     * each user has a password encoded with a different encoder. the encoder is identified by the key between the first {}
     * @param auth
     * @throws Exception
     */
 /*   @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.inMemoryAuthentication()
                .withUser("foxtrot")
                .password("{bcrypt}$2a$10$le25QH9GJts418aHRH8OFeZWelrAEFd50.s8dsPleDrKQzNG93kcq") //secret
                .roles("ADMIN")
                .and()
                .withUser("user")
                .password("{sha256}946f099b97802346a517649a6099f12650ba5f99ef454280ea2b6e65a3021fad0e244cd685a18ca5") //password
                .roles("USER")
                .and()
                .withUser("scott")
                .password("{bcrypt15}$2a$15$IUBozzcPKZopen5.xGZwxO71.VjarS4BK1tH6lroPfB4/2PIYLz7C") //tiger
                .roles("CUSTOMER");


    }

  */


}
