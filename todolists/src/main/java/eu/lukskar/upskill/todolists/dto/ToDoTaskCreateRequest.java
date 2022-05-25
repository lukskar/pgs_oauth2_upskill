package eu.lukskar.upskill.todolists.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Jacksonized @Builder
public class ToDoTaskCreateRequest {
    private final String name;
    private final String dueDate;
}
