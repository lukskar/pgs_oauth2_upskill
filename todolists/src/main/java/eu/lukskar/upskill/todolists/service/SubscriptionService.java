package eu.lukskar.upskill.todolists.service;

import eu.lukskar.upskill.todolists.dto.SubscriptionDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class SubscriptionService {

    private final WebClient subscriptionClient;

    public SubscriptionService(@Qualifier("subscriptionServiceClient") final WebClient subscriptionClient) {
        this.subscriptionClient = subscriptionClient;
    }

    public boolean isUserSubscriptionActive(String userId) {
        SubscriptionDto subscriptionDto = subscriptionClient.get()
                .uri("http://localhost:9090/api/user/{userId}/subscription", userId)
                .retrieve()
                .bodyToMono(SubscriptionDto.class)
                .block();

        return Instant.parse(subscriptionDto.getEndDateTime()).isAfter(Instant.now());
    }

    public void createTrialSubscription(String userId) {
        Instant now = Instant.now().truncatedTo(ChronoUnit.SECONDS);
        SubscriptionDto trialSubscription = SubscriptionDto.builder()
                .startDateTime(now.toString())
                .endDateTime(now.plus(7L, ChronoUnit.DAYS).toString())
                .build();

        subscriptionClient.post()
                .uri("http://localhost:9090/api/user/{userId}/subscription", userId)
                .bodyValue(trialSubscription)
                .retrieve()
                .bodyToMono(SubscriptionDto.class)
                .block();
    }
}
