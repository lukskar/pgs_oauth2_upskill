package eu.lukskar.upskill.todolists.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document("ToDoTask")
public class ToDoTask {
    @Id private String id;
    private String name;
    private String dueDate;
    private boolean done;
}
