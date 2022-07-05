package eu.lukskar.upskill.todolists.config;

import eu.lukskar.upskill.todolists.service.OAuth2AuthenticationManager;
import eu.lukskar.upskill.todolists.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

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
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionCustomizer -> sessionCustomizer.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .authorizeRequests(requestCustomizer -> requestCustomizer
                        .antMatchers("/task**").authenticated()
                        .anyRequest().permitAll())
                .httpBasic(AbstractHttpConfigurer::disable)
                .build();
    }
}
