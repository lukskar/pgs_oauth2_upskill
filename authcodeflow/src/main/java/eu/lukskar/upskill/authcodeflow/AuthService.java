package eu.lukskar.upskill.authcodeflow;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.lukskar.upskill.authcodeflow.dto.AuthorizationResponse;
import eu.lukskar.upskill.authcodeflow.dto.SimpleJWTPayload;
import eu.lukskar.upskill.authcodeflow.state.LoggedUserService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;

@Service
public class AuthService {

    private final ObjectMapper objectMapper;
    private final LoggedUserService loggedUserService;

    private final String AUTH0_DOMAIN = "https://dev--eofa3x5.us.auth0.com";
    private final String REDIRECT_URI = "https://6848-188-114-87-11.eu.ngrok.io/auth/callback";
    private final String CLIENT_ID = "P614y4vJHfyGe8FEU7eOQX1qAPp70tpe";
    private final String CLIENT_SECRET = "SAPKOfNipjvM1ItLkoigTv7K3AKKIuaxYehKzt_koJpA8RLf5aTcStGHTjAcMTK5";

    public AuthService(ObjectMapper objectMapper, LoggedUserService loggedUserService) {
        this.objectMapper = objectMapper;
        this.loggedUserService = loggedUserService;
    }

    public String buildLoginUrl() {
        return String.format("%s/authorize?" +
                        "response_type=code&" +
                        "client_id=%s&" +
                        "scope=openid profile&" +
                        "redirect_uri=%s",
                AUTH0_DOMAIN, CLIENT_ID, REDIRECT_URI);
    }

    public String handleAuthorizationCallback(String code) throws IOException, InterruptedException {
        AuthorizationResponse authorizationResponse = this.exchangeAuthorizationCodes(code);
        SimpleJWTPayload jwtPayload = this.parseJwtPayload(authorizationResponse.getIdToken());
        return loggedUserService.logUserIn(jwtPayload);
    }

    private AuthorizationResponse exchangeAuthorizationCodes(String code) throws IOException, InterruptedException {
        String reqBody = buildAuthTokenRequestBody(code);

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(String.format("%s/oauth/token", AUTH0_DOMAIN)))
                .POST(HttpRequest.BodyPublishers.ofString(reqBody))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .build();
        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        return objectMapper.readValue(response.body(), AuthorizationResponse.class);
    }

    private String buildAuthTokenRequestBody(String code) {
        return String.format(
                "grant_type=authorization_code&" +
                "client_id=%s&" +
                "client_secret=%s&" +
                "code=%s&" +
                "redirect_uri=%s",
            CLIENT_ID, CLIENT_SECRET, code, REDIRECT_URI
        );
    }

    private SimpleJWTPayload parseJwtPayload(String idToken) throws JsonProcessingException {
        String encodedJwtPayload = idToken.split("\\.")[1];
        Base64.Decoder decoder = Base64.getUrlDecoder();
        String decodedJwtPayload = new String(decoder.decode(encodedJwtPayload));
        return objectMapper.readValue(decodedJwtPayload, SimpleJWTPayload.class);
    }
}
