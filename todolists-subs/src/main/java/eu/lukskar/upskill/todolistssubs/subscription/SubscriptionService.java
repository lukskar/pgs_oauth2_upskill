package eu.lukskar.upskill.todolistssubs.subscription;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    public SubscriptionService(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    public SubscriptionDto getSubscription(final String userId) {
        return subscriptionRepository.findByUserId(userId)
                .map(this::toDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public SubscriptionDto postSubscription(final String userId, final SubscriptionDto subscriptionDto) {
        Optional<Subscription> dbSubscription = subscriptionRepository.findByUserId(userId);

        if (dbSubscription.isPresent()) {
            Subscription existingSubscription = dbSubscription.get();
            existingSubscription.setStartDateTime(subscriptionDto.getStartDateTime());
            existingSubscription.setEndDateTime(subscriptionDto.getEndDateTime());
            subscriptionRepository.save(existingSubscription);
            return toDto(existingSubscription);
        } else {
            Subscription newSubscription = Subscription.builder()
                    .id(null)
                    .userId(userId)
                    .startDateTime(subscriptionDto.getStartDateTime())
                    .endDateTime(subscriptionDto.getEndDateTime())
                    .build();
            subscriptionRepository.save(newSubscription);
            return toDto(newSubscription);
        }
    }

    private SubscriptionDto toDto(Subscription subscription) {
        return SubscriptionDto.builder()
                .startDateTime(subscription.getStartDateTime())
                .endDateTime(subscription.getEndDateTime())
                .build();
    }
}
