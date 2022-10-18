package academy.devdojo.springboot.domain.Config;

import academy.devdojo.springboot.domain.Service.DevDojoUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

/*security filters:
 * BasicAuthenticationFilter
 * UsernameAndPasswordAuthenticationFilter
 * DefaultLoginPageGeneratingFilter
 * DefaultLogOutPageGeneratingFilter
 * FilterSecurityInterceptor
 * Authentication -> Authorization*/

@EnableWebSecurity
@Slf4j
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final DevDojoUserDetailsService devDojoUserDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                //.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .authorizeHttpRequests()
                .antMatchers("/animes/admin/**").hasRole("ADMIN")
                .antMatchers("/animes/").hasRole("USER")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .and()
                .httpBasic();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        PasswordEncoder delegatingPasswordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        log.info("encoded password {}", delegatingPasswordEncoder.encode("academy"));
        //In Memory Authentication
        auth.inMemoryAuthentication()
                .withUser("admin")
                .password(delegatingPasswordEncoder.encode("adminz"))
                .roles("USER")
                .and()
                .withUser("secret")
                .password(delegatingPasswordEncoder.encode("secret"))
                .roles("USER", "ADMIN");
        //Data persistence authentication
        auth.userDetailsService(devDojoUserDetailsService).passwordEncoder(delegatingPasswordEncoder);
    }
}
