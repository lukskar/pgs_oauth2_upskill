package eu.lukskar.upskill.todolists;

import org.springdoc.core.SpringDocUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;

@SpringBootApplication
public class ToDoListsApplication {

	public static void main(String[] args) {
		SpringDocUtils.getConfig().addAnnotationsToIgnore(RegisteredOAuth2AuthorizedClient.class);
		SpringApplication.run(ToDoListsApplication.class, args);
	}

}
