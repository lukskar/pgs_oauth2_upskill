package eu.lukskar.upskill.todolists.config;

import eu.lukskar.upskill.todolists.model.RegistrationType;
import eu.lukskar.upskill.todolists.service.OAuth2AuthenticationManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

@Configuration
public class SecurityConfig {

    @Autowired
    private OAuth2AuthenticationManager oAuth2AuthenticationManager;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .formLogin(formLoginCustomizer -> formLoginCustomizer
                        .loginPage("/login.html")
                        .defaultSuccessUrl("/login", true))
                .oauth2Login(oauth2Customizer -> oauth2Customizer
                        .loginPage("/login.html")
                        .defaultSuccessUrl("/login", true)
                        .userInfoEndpoint().userService(oAuth2AuthenticationManager))
                .logout(logoutCustomizer -> logoutCustomizer
                        .logoutUrl("/logout.html")
                        .logoutSuccessUrl("/login")
                        .clearAuthentication(true)
                        .invalidateHttpSession(true))
                .exceptionHandling(exceptionHandlingCustomizer -> exceptionHandlingCustomizer
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionCustomizer -> sessionCustomizer.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .authorizeRequests(requestCustomizer -> requestCustomizer
                        .antMatchers("/task**").authenticated()
                        .antMatchers("/user/info").hasAuthority(RegistrationType.OAUTH2_GOOGLE)
                        .anyRequest().permitAll())
                .httpBasic(AbstractHttpConfigurer::disable)
                .build();
    }
}
