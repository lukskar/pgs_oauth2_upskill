package eu.lukskar.upskill.todolists.config;

import eu.lukskar.upskill.todolists.model.RegistrationType;
import eu.lukskar.upskill.todolists.service.LogoutHandler;
import eu.lukskar.upskill.todolists.service.OidcUsersManagerService;
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
    private OidcUsersManagerService oidcUsersManagerService;

    @Autowired
    private LogoutHandler logoutHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .oauth2Login(oauth2Customizer -> oauth2Customizer
                        .defaultSuccessUrl("/login", true)
                        .userInfoEndpoint()
                        .oidcUserService(oidcUsersManagerService))
                .logout(logoutCustomizer -> logoutCustomizer
                        .logoutUrl("/logout")
                        .addLogoutHandler(logoutHandler))
                .exceptionHandling(exceptionHandlingCustomizer -> exceptionHandlingCustomizer
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionCustomizer -> sessionCustomizer.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .authorizeRequests(requestCustomizer -> requestCustomizer
                        .antMatchers("/task**").authenticated()
                        .antMatchers("/task/*/reminder").hasAuthority(RegistrationType.OAUTH2_GOOGLE)
                        .antMatchers("/user/info").hasAuthority(RegistrationType.OAUTH2_GOOGLE)
                        .anyRequest().permitAll())
                .httpBasic(AbstractHttpConfigurer::disable)
                .build();
    }
}
