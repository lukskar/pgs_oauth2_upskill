package eu.lukskar.upskill.todolists.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Jacksonized @Builder
public class CreateReminderRequest {
    private final String dateTime;
}
