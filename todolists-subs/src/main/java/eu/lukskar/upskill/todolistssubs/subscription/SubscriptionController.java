package eu.lukskar.upskill.todolistssubs.subscription;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @GetMapping(value = "/api/user/{userId}/subscription")
    public SubscriptionDto getSubscription(@PathVariable final String userId) {
        return subscriptionService.getSubscription(userId);
    }

    @PostMapping(value = "/api/user/{userId}/subscription")
    public SubscriptionDto postSubscription(@PathVariable final String userId, @RequestBody final SubscriptionDto subscriptionDto) {
        return subscriptionService.postSubscription(userId, subscriptionDto);
    }
}
