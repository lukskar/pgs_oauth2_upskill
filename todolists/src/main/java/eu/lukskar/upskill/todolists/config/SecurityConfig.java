package eu.lukskar.upskill.todolists.config;

import eu.lukskar.upskill.todolists.model.RegistrationType;
import eu.lukskar.upskill.todolists.service.LogoutHandler;
import eu.lukskar.upskill.todolists.service.OidcUsersManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder.ClientCredentialsGrantBuilder;
import org.springframework.security.oauth2.client.endpoint.DefaultClientCredentialsTokenResponseClient;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.function.Consumer;

@Configuration
public class SecurityConfig {

    @Value("${spring.security.oauth2.client.provider.subscription.audience}")
    private String subscriptionServiceAudience;

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

    @Bean(name = "subscriptionServiceClient")
    public WebClient subscriptionServiceClient(ClientRegistrationRepository clientRegistrationRepository) {
        var clientService = new InMemoryOAuth2AuthorizedClientService(clientRegistrationRepository);
        var authorizedClientManager = new AuthorizedClientServiceOAuth2AuthorizedClientManager(clientRegistrationRepository, clientService);
        authorizedClientManager.setAuthorizedClientProvider(authorizedClientProviderWithAudience(subscriptionServiceAudience));

        var oauth = new ServletOAuth2AuthorizedClientExchangeFilterFunction(authorizedClientManager);
        oauth.setDefaultClientRegistrationId("subscription");
        return WebClient.builder().filter(oauth).build();
    }

    private OAuth2AuthorizedClientProvider authorizedClientProviderWithAudience(final String audience) {
        var clientCredentialsTokenResponseClient = new DefaultClientCredentialsTokenResponseClient();
        var customRequestEntityConverter = new Auth0ClientCredentialsGrantRequestEntityConverter(audience);
        var clientCredentialsBuilder = (Consumer<ClientCredentialsGrantBuilder>) clientCredentialsGrantBuilder -> {
            clientCredentialsTokenResponseClient.setRequestEntityConverter(customRequestEntityConverter);
            clientCredentialsGrantBuilder.accessTokenResponseClient(clientCredentialsTokenResponseClient);
        };

        return OAuth2AuthorizedClientProviderBuilder.builder()
                .refreshToken()
                .clientCredentials(clientCredentialsBuilder)
                .build();
    }
}
