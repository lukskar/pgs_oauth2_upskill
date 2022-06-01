package eu.lukskar.upskill.todolists.controller;

import eu.lukskar.upskill.todolists.dto.ToDoTaskCreateRequest;
import eu.lukskar.upskill.todolists.dto.ToDoTaskUpdateRequest;
import eu.lukskar.upskill.todolists.model.DbUserDetails;
import eu.lukskar.upskill.todolists.model.ToDoTask;
import eu.lukskar.upskill.todolists.service.ToDoTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class ToDoTaskController {

    private final ToDoTaskService toDoTaskService;

    public ToDoTaskController(ToDoTaskService toDoTaskService) {
        this.toDoTaskService = toDoTaskService;
    }

    @GetMapping("/task")
    public List<ToDoTask> getTasks(@AuthenticationPrincipal final DbUserDetails userDetails) {
        return toDoTaskService.getTasks(userDetails.getId());
    }

    @GetMapping("/task/{taskId}")
    public ToDoTask getTask(@PathVariable final String taskId, @AuthenticationPrincipal final DbUserDetails userDetails) {
        return toDoTaskService.getTask(userDetails.getId(), taskId);
    }

    @PostMapping("/task")
    public ToDoTask createTask(@RequestBody final ToDoTaskCreateRequest toDoTaskCreateRequest,
                               @AuthenticationPrincipal final DbUserDetails userDetails) {
        return toDoTaskService.createTask(userDetails.getId(), toDoTaskCreateRequest);
    }

    @PutMapping("/task/{taskId}")
    public ToDoTask updateTask(@PathVariable final String taskId,
                               @RequestBody final ToDoTaskUpdateRequest toDoTaskUpdateRequest,
                               @AuthenticationPrincipal final DbUserDetails userDetails) {
        return toDoTaskService.updateTask(userDetails.getId(), taskId, toDoTaskUpdateRequest);
    }

    @DeleteMapping("/task/{taskId}")
    public void deleteTask(@PathVariable final String taskId, @AuthenticationPrincipal final DbUserDetails userDetails) {
        toDoTaskService.deleteTask(userDetails.getId(), taskId);
    }
}
