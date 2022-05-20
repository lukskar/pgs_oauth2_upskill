package eu.lukskar.upskill.jwttimeapi.api;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import eu.lukskar.upskill.jwttimeapi.dto.UserCredentials;
import eu.lukskar.upskill.jwttimeapi.model.UserDetails;
import eu.lukskar.upskill.jwttimeapi.repository.UserDetailsRepository;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletResponse;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@RestController
public class AuthController {

    private final UserDetailsRepository userDetailsRepository;

    public AuthController(UserDetailsRepository userDetailsRepository) {
        this.userDetailsRepository = userDetailsRepository;
    }

    @PostMapping("/auth/login")
    public void login(@RequestBody final UserCredentials userCredentials, HttpServletResponse httpResponse) {
        Example<UserDetails> example = Example.of(new UserDetails(null, userCredentials.getUsername(), null));
        UserDetails userDetails = userDetailsRepository.findOne(example).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (!passwordEncoder.matches(userCredentials.getPassword(), userDetails.getPasswordHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        Algorithm algorithm = Algorithm.HMAC256(System.getenv("JWT_SECRET"));
        String token = JWT.create()
                .withIssuer("eu.lukskar.upskill.jwttimeapi")
                .withSubject(userCredentials.getUsername())
                .withExpiresAt(Date.from(Instant.now().plus(1, ChronoUnit.DAYS)))
                .sign(algorithm);

        httpResponse.addHeader("Authorization", token);
    }

    @PostMapping("/auth/register")
    public void register(@RequestBody final UserCredentials userCredentials) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        UserDetails userDetails = new UserDetails();

        userDetails.setUserName(userCredentials.getUsername());
        userDetails.setPasswordHash(passwordEncoder.encode(userCredentials.getPassword()));

        userDetailsRepository.save(userDetails);
    }
}
