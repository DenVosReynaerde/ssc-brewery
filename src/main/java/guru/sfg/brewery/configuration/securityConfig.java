package guru.sfg.brewery.configuration;

import guru.sfg.brewery.security.FoxtrotPasswordEncoderFactories;
import guru.sfg.brewery.security.RestHeaderAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class securityConfig extends WebSecurityConfigurerAdapter {

    public RestHeaderAuthFilter restHeaderAuthFilter(AuthenticationManager authenticationManager) {
        RestHeaderAuthFilter filter = new RestHeaderAuthFilter(new AntPathRequestMatcher("/api/**"));
        filter.setAuthenticationManager(authenticationManager);

        return filter;
    }

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

        http.addFilterBefore(restHeaderAuthFilter(authenticationManager()),
                UsernamePasswordAuthenticationFilter.class)
        .csrf().disable();
        http.authorizeRequests(authorize -> {
            authorize
                    .antMatchers("/webjars/**", "/login", "/resources/**").permitAll()
                    .antMatchers("/", "/beers/find", "/beers*").permitAll()
                    .antMatchers(HttpMethod.GET, "/api/v1/beer/**").permitAll()
                    .mvcMatchers(HttpMethod.GET, "/api/v1/beerUpc/{upc}").permitAll();
        })
        .authorizeRequests(
                (requests) -> requests.anyRequest()
                        .authenticated());
        http.formLogin();
        http.httpBasic();
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
    @Override
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
}
