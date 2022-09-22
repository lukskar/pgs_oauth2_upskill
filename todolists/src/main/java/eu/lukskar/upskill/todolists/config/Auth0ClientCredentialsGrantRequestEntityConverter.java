package eu.lukskar.upskill.todolists.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.security.oauth2.client.endpoint.OAuth2ClientCredentialsGrantRequest;
import org.springframework.security.oauth2.client.endpoint.OAuth2ClientCredentialsGrantRequestEntityConverter;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Collections;

/**
 * <p>This class is a copy of {@link OAuth2ClientCredentialsGrantRequestEntityConverter} and
 * {@link org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationGrantRequestEntityUtils} just
 * to add the {code audience} to the Authorization request sent to Auth0.</p>
 * <p>Sadly these classes aren't extensible and thus the copy/paste.</p>
 *
 * @see <a href="https://github.com/spring-projects/spring-security/issues/6569">issue 6569</a>
 * @see <a href="https://github.com/spring-projects/spring-security/issues/7379">issue 7379</a>
 * @since 1.1.0
 */
public final class Auth0ClientCredentialsGrantRequestEntityConverter
        implements Converter<OAuth2ClientCredentialsGrantRequest, RequestEntity<?>> {

    private static final HttpHeaders DEFAULT_TOKEN_REQUEST_HEADERS = getDefaultTokenRequestHeaders();

    private final String audience;

    /**
     * @param audience The audience to pass to Auth0
     */
    public Auth0ClientCredentialsGrantRequestEntityConverter(String audience) {
        this.audience = audience;
    }

    /**
     * Returns the {@link RequestEntity} used for the Access Token Request.
     *
     * @param clientCredentialsGrantRequest the client credentials grant request
     * @return the {@link RequestEntity} used for the Access Token Request
     */
    @Override
    public RequestEntity<?> convert(OAuth2ClientCredentialsGrantRequest clientCredentialsGrantRequest) {
        ClientRegistration clientRegistration = clientCredentialsGrantRequest.getClientRegistration();
        HttpHeaders headers = getTokenRequestHeaders(clientRegistration);
        MultiValueMap<String, String> formParameters = this.buildFormParameters(clientCredentialsGrantRequest);
        URI uri = UriComponentsBuilder.fromUriString(clientRegistration.getProviderDetails().getTokenUri()).build().toUri();
        return new RequestEntity<>(formParameters, headers, HttpMethod.POST, uri);
    }

    /**
     * Returns a {@link MultiValueMap} of the form parameters used for the Access Token
     * Request body.
     *
     * @param clientCredentialsGrantRequest the client credentials grant request
     * @return a {@link MultiValueMap} of the form parameters used for the Access Token
     * Request body
     */
    private MultiValueMap<String, String> buildFormParameters(
            OAuth2ClientCredentialsGrantRequest clientCredentialsGrantRequest) {
        ClientRegistration clientRegistration = clientCredentialsGrantRequest.getClientRegistration();
        MultiValueMap<String, String> formParameters = new LinkedMultiValueMap<>();

        if (!CollectionUtils.isEmpty(clientRegistration.getScopes())) {
            formParameters.add(OAuth2ParameterNames.SCOPE,
                    StringUtils.collectionToDelimitedString(clientRegistration.getScopes(), " "));
        }

        formParameters.add(OAuth2ParameterNames.GRANT_TYPE, clientCredentialsGrantRequest.getGrantType().getValue());
        formParameters.add(OAuth2ParameterNames.CLIENT_ID, clientRegistration.getClientId());
        formParameters.add(OAuth2ParameterNames.CLIENT_SECRET, clientRegistration.getClientSecret());
        formParameters.add("audience", this.audience);
        return formParameters;
    }

    private static HttpHeaders getTokenRequestHeaders(ClientRegistration clientRegistration) {
        HttpHeaders headers = new HttpHeaders();
        headers.addAll(DEFAULT_TOKEN_REQUEST_HEADERS);
        if (ClientAuthenticationMethod.CLIENT_SECRET_BASIC.equals(clientRegistration.getClientAuthenticationMethod())) {
            headers.setBasicAuth(clientRegistration.getClientId(), clientRegistration.getClientSecret());
        }
        return headers;
    }

    private static HttpHeaders getDefaultTokenRequestHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        final MediaType contentType = MediaType.valueOf(MediaType.APPLICATION_FORM_URLENCODED_VALUE + ";charset=UTF-8");
        headers.setContentType(contentType);
        return headers;
    }

}