package eu.lukskar.upskill.todolists.service;

import eu.lukskar.upskill.todolists.dto.ToDoTaskCreateRequest;
import eu.lukskar.upskill.todolists.dto.ToDoTaskUpdateRequest;
import eu.lukskar.upskill.todolists.model.ToDoTask;
import eu.lukskar.upskill.todolists.repository.ToDoTaskRepository;
import eu.lukskar.upskill.todolists.state.AppState;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ToDoTaskService {

    private final AppState appState;
    private final ToDoTaskRepository toDoTaskRepository;

    public ToDoTaskService(AppState appState, final ToDoTaskRepository toDoTaskRepository) {
        this.appState = appState;
        this.toDoTaskRepository = toDoTaskRepository;
    }

    public List<ToDoTask> getTasks(final String JSESSIONID) {
        String userId = appState.getLoggedUserIdOrThrow(JSESSIONID);
        return toDoTaskRepository.findAllByUserId(userId);
    }

    public ToDoTask getTask(final String JSESSIONID, final String taskId) {
        String userId = appState.getLoggedUserIdOrThrow(JSESSIONID);
        return toDoTaskRepository
                .findByIdAndUserId(taskId, userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public ToDoTask createTask(final String JSESSIONID, final ToDoTaskCreateRequest toDoTaskCreateRequest) {
        String userId = appState.getLoggedUserIdOrThrow(JSESSIONID);

        ToDoTask newTask = ToDoTask.builder()
                .userId(userId)
                .name(toDoTaskCreateRequest.getName())
                .dueDate(toDoTaskCreateRequest.getDueDate())
                .done(false)
                .build();

        return toDoTaskRepository.insert(newTask);
    }

    public ToDoTask updateTask(final String JSESSIONID, final String taskId, final ToDoTaskUpdateRequest toDoTaskUpdateRequest) {
        ToDoTask toUpdate = getTask(JSESSIONID, taskId);
        toUpdate.setName(toDoTaskUpdateRequest.getName());
        toUpdate.setDueDate(toDoTaskUpdateRequest.getDueDate());
        toUpdate.setDone(toDoTaskUpdateRequest.isDone());

        return toDoTaskRepository.save(toUpdate);
    }

    public void deleteTask(final String JSESSIONID, final String taskId) {
        String userId = appState.getLoggedUserIdOrThrow(JSESSIONID);
        toDoTaskRepository.deleteByIdAndUserId(taskId, userId);
    }
}
