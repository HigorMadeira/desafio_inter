package com.higor.desafiointer.task.api;

import com.higor.desafiointer.task.api.dto.CreateTaskRequest;
import com.higor.desafiointer.task.api.dto.TaskResponse;
import com.higor.desafiointer.task.api.dto.UpdateTaskRequest;
import com.higor.desafiointer.task.application.TaskService;
import com.higor.desafiointer.task.application.command.CreateTaskCommand;
import com.higor.desafiointer.task.application.command.UpdateTaskCommand;
import com.higor.desafiointer.task.domain.Task;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tasks")
@Tag(name = "Tasks", description = "Endpoints para gerenciamento de tarefas")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @Operation(summary = "Cria uma nova tarefa")
    @PostMapping
    public ResponseEntity<TaskResponse> create(@Valid @RequestBody CreateTaskRequest request) {
        Task task = taskService.create(new CreateTaskCommand(
                request.title(),
                request.description()
        ));

        URI location = URI.create("/api/tasks/" + task.getId());

        return ResponseEntity
                .created(location)
                .body(TaskResponse.fromDomain(task));
    }

    @Operation(summary = "Lista todas as tarefas")
    @GetMapping
    public ResponseEntity<List<TaskResponse>> findAll() {
        List<TaskResponse> response = taskService.findAll()
                .stream()
                .map(TaskResponse::fromDomain)
                .toList();

        return ResponseEntity.ok(response);
    }
    @Operation(summary = "Busca tarefa por ID")
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> findById(@PathVariable UUID id) {
        Task task = taskService.findById(id);

        return ResponseEntity.ok(TaskResponse.fromDomain(task));
    }

    @Operation(summary = "Atualiza uma tarefa existente")
    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateTaskRequest request
    ) {
        Task task = taskService.update(new UpdateTaskCommand(
                id,
                request.title(),
                request.description(),
                request.status()
        ));

        return ResponseEntity.ok(TaskResponse.fromDomain(task));
    }
    @Operation(summary = "Remove uma tarefa")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        taskService.delete(id);

        return ResponseEntity.noContent().build();
    }
}