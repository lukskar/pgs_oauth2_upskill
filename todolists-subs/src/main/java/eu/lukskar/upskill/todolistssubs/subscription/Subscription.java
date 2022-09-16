package eu.lukskar.upskill.todolistssubs.subscription;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document("ToDoSubscription")
public class Subscription {
    @Id private String id;
    private String userId;
    private String startDateTime;
    private String endDateTime;
}
