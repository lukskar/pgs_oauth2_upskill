package eu.lukskar.upskill.todolists.service;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

@Service
public class Auth0ManagementService {

    @Value("${spring.security.oauth2.client.provider.auth0.issuer-uri}")
    private String issuerUri;

    @Value("${spring.security.oauth2.client.provider.auth0.management-client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.provider.auth0.management-client-secret}")
    private String clientSecret;

    public String getIdPAccessToken(String auth0Subject, String providerName) throws UnirestException {
        String managementAccessToken = getAuth0ManagementAccessToken();
        JSONArray idProviders = getIdentityProvidersArray(auth0Subject, managementAccessToken);
        return getIdentityProviderAccessToken(idProviders, providerName);
    }

    private String getAuth0ManagementAccessToken() throws UnirestException {
        return Unirest.post(auth0TokenEndpoint())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(String.format("{\"client_id\":\"%s\",\"client_secret\":\"%s\",\"audience\":\"%s\",\"grant_type\":\"%s\"}",
                        clientId, clientSecret, auth0ManagementAudience(), "client_credentials"))
                .asJson()
                .getBody()
                .getObject()
                .get("access_token")
                .toString();
    }

    private JSONArray getIdentityProvidersArray(String auth0Subject, String managementAccessToken) throws UnirestException {
        return Unirest.get(auth0UsersEndpoint() + auth0Subject)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + managementAccessToken)
                .asJson()
                .getBody()
                .getObject()
                .getJSONArray("identities");
    }

    private String getIdentityProviderAccessToken(JSONArray idProviders, String providerName) {
        for (int i = 0; i < idProviders.length(); i++) {
            JSONObject idProvider = idProviders.getJSONObject(i);
            if (providerName.equals(idProvider.get("provider").toString())) {
                return idProvider.get("access_token").toString();
            }
        }

        return null;
    }

    private String auth0TokenEndpoint() {
        return issuerUri.concat("oauth/token");
    }

    private String auth0UsersEndpoint() {
        return issuerUri.concat("api/v2/users/");
    }

    private String auth0ManagementAudience() {
        return issuerUri.concat("api/v2/");
    }
}
