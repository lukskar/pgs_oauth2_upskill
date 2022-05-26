package eu.lukskar.upskill.todolists.controller;

import eu.lukskar.upskill.todolists.dto.ToDoTaskCreateRequest;
import eu.lukskar.upskill.todolists.dto.ToDoTaskUpdateRequest;
import eu.lukskar.upskill.todolists.model.ToDoTask;
import eu.lukskar.upskill.todolists.service.ToDoTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestController
public class ToDoTaskController implements SessionAwareController {

    private final ToDoTaskService toDoTaskService;

    public ToDoTaskController(ToDoTaskService toDoTaskService) {
        this.toDoTaskService = toDoTaskService;
    }

    @GetMapping("/task")
    public List<ToDoTask> getTasks(HttpServletRequest request) {
        return toDoTaskService.getTasks(getSessionId(request));
    }

    @GetMapping("/task/{taskId}")
    public ToDoTask getTask(@PathVariable final String taskId,
                            HttpServletRequest request) {
        return toDoTaskService.getTask(getSessionId(request), taskId);
    }

    @PostMapping("/task")
    public ToDoTask createTask(@RequestBody final ToDoTaskCreateRequest toDoTaskCreateRequest,
                               HttpServletRequest request) {
        return toDoTaskService.createTask(getSessionId(request), toDoTaskCreateRequest);
    }

    @PutMapping("/task/{taskId}")
    public ToDoTask updateTask(@PathVariable final String taskId,
                               @RequestBody final ToDoTaskUpdateRequest toDoTaskUpdateRequest,
                               HttpServletRequest request) {
        return toDoTaskService.updateTask(getSessionId(request), taskId, toDoTaskUpdateRequest);
    }

    @DeleteMapping("/task/{taskId}")
    public void deleteTask(@PathVariable final String taskId,
                           HttpServletRequest request) {
        toDoTaskService.deleteTask(getSessionId(request), taskId);
    }
}
