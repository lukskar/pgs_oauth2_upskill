package eu.lukskar.upskill.todolists.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.endpoint.DefaultClientCredentialsTokenResponseClient;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.function.Consumer;

@Configuration
public class OAuthClientConfig {

    @Value("${spring.security.oauth2.client.provider.subscription.audience}")
    private String subscriptionServiceAudience;

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
        var clientCredentialsBuilder = (Consumer<OAuth2AuthorizedClientProviderBuilder.ClientCredentialsGrantBuilder>) clientCredentialsGrantBuilder -> {
            clientCredentialsTokenResponseClient.setRequestEntityConverter(customRequestEntityConverter);
            clientCredentialsGrantBuilder.accessTokenResponseClient(clientCredentialsTokenResponseClient);
        };

        return OAuth2AuthorizedClientProviderBuilder.builder()
                .refreshToken()
                .clientCredentials(clientCredentialsBuilder)
                .build();
    }
}
