package eu.lukskar.upskill.todolists.service;

import com.google.api.services.calendar.model.Event;
import com.mashape.unirest.http.exceptions.UnirestException;
import eu.lukskar.upskill.todolists.dto.AuthUserDetails;
import eu.lukskar.upskill.todolists.dto.CreateReminderRequest;
import eu.lukskar.upskill.todolists.dto.CreateReminderResponse;
import eu.lukskar.upskill.todolists.dto.ToDoTaskCreateRequest;
import eu.lukskar.upskill.todolists.dto.ToDoTaskUpdateRequest;
import eu.lukskar.upskill.todolists.model.RegistrationType;
import eu.lukskar.upskill.todolists.model.ToDoTask;
import eu.lukskar.upskill.todolists.repository.ToDoTaskRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@Service
public class ToDoTaskService {

    private final ToDoTaskRepository toDoTaskRepository;
    private final Auth0ManagementService auth0ManagementService;
    private final GoogleCalendarService calendarService;
    private final SubscriptionService subscriptionService;

    public ToDoTaskService(final ToDoTaskRepository toDoTaskRepository, final Auth0ManagementService auth0ManagementService,
                           final GoogleCalendarService calendarService, final SubscriptionService subscriptionService) {
        this.toDoTaskRepository = toDoTaskRepository;
        this.auth0ManagementService = auth0ManagementService;
        this.calendarService = calendarService;
        this.subscriptionService = subscriptionService;
    }

    public List<ToDoTask> getTasks(final String userId) {
        return toDoTaskRepository.findAllByUserId(userId);
    }

    public ToDoTask getTask(final String userId, final String taskId) {
        return toDoTaskRepository
                .findByIdAndUserId(taskId, userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public ToDoTask createTask(final String userId, final ToDoTaskCreateRequest toDoTaskCreateRequest) {
        ToDoTask newTask = ToDoTask.builder()
                .userId(userId)
                .name(toDoTaskCreateRequest.getName())
                .dueDate(toDoTaskCreateRequest.getDueDate())
                .done(false)
                .build();

        return toDoTaskRepository.insert(newTask);
    }

    public ToDoTask updateTask(final String userId, final String taskId, final ToDoTaskUpdateRequest toDoTaskUpdateRequest) {
        ToDoTask toUpdate = getTask(userId, taskId);
        toUpdate.setName(toDoTaskUpdateRequest.getName());
        toUpdate.setDueDate(toDoTaskUpdateRequest.getDueDate());
        toUpdate.setDone(toDoTaskUpdateRequest.isDone());

        return toDoTaskRepository.save(toUpdate);
    }

    public void deleteTask(final String userId, final String taskId) {
        toDoTaskRepository.deleteByIdAndUserId(taskId, userId);
    }

    public CreateReminderResponse createReminder(final String userId,
                                                 final String taskId,
                                                 final CreateReminderRequest reminderRequest,
                                                 final AuthUserDetails userDetails)
            throws GeneralSecurityException, IOException, UnirestException {
        ToDoTask toRemind = getTask(userId, taskId);
        String googleAccessToken = auth0ManagementService.getIdPAccessToken(userDetails.getUserInfo().getSubject(), RegistrationType.OAUTH2_GOOGLE);
        Event inserted = calendarService.publishReminderFor(toRemind, reminderRequest.getDateTime(), googleAccessToken);
        return CreateReminderResponse.builder()
                .reminderLink(inserted.getHtmlLink())
                .build();
    }
}
