package eu.lukskar.upskill.jwttimeapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class JwtTimeApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(JwtTimeApiApplication.class, args);
	}

}
