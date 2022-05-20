package eu.lukskar.upskill.jwttimeapi.api;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import eu.lukskar.upskill.jwttimeapi.dto.TimeInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

@RestController
public class ApiController {

    @GetMapping("/api/time")
    @Operation(security = @SecurityRequirement(name = "jwtauth"))
    public TimeInfo getTimeInfo(@RequestHeader HttpHeaders headers) {
        try {
            List<String> authHeaders = headers.get("Authorization");
            String jwtToken = authHeaders == null || authHeaders.isEmpty() ?
                    "" : authHeaders.get(0).substring(7);

            Algorithm algorithm = Algorithm.HMAC256(System.getenv("JWT_SECRET"));
            DecodedJWT decodedJwt = JWT.require(algorithm)
                    .withIssuer("eu.lukskar.upskill.jwttimeapi")
                    .build()
                    .verify(jwtToken);
        } catch (JWTVerificationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        return new TimeInfo(
                Instant.now()
                        .atZone(ZoneId.of("Europe/Warsaw"))
                        .truncatedTo(ChronoUnit.SECONDS)
                        .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        );
    }
}
