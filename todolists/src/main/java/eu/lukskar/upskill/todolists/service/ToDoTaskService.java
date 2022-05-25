package eu.lukskar.upskill.todolists.service;

import eu.lukskar.upskill.todolists.dto.ToDoTaskCreateRequest;
import eu.lukskar.upskill.todolists.dto.ToDoTaskUpdateRequest;
import eu.lukskar.upskill.todolists.model.ToDoTask;
import eu.lukskar.upskill.todolists.repository.ToDoTaskRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ToDoTaskService {

    private final ToDoTaskRepository toDoTaskRepository;

    public ToDoTaskService(final ToDoTaskRepository toDoTaskRepository) {
        this.toDoTaskRepository = toDoTaskRepository;
    }

    public List<ToDoTask> getTasks() {
        return toDoTaskRepository.findAll();
    }

    public ToDoTask getTask(final String taskId) {
        return toDoTaskRepository
                .findById(taskId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public ToDoTask createTask(final ToDoTaskCreateRequest toDoTaskCreateRequest) {
        ToDoTask newTask = ToDoTask.builder()
                .name(toDoTaskCreateRequest.getName())
                .dueDate(toDoTaskCreateRequest.getDueDate())
                .done(false)
                .build();

        return toDoTaskRepository.insert(newTask);
    }

    public ToDoTask updateTask(final String taskId, final ToDoTaskUpdateRequest toDoTaskUpdateRequest) {
        ToDoTask toUpdate = getTask(taskId);
        toUpdate.setName(toDoTaskUpdateRequest.getName());
        toUpdate.setDueDate(toDoTaskUpdateRequest.getDueDate());
        toUpdate.setDone(toDoTaskUpdateRequest.isDone());

        return toDoTaskRepository.save(toUpdate);
    }

    public void deleteTask(final String taskId) {
        toDoTaskRepository.deleteById(taskId);
    }
}
