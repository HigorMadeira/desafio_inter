package com.higor.desafiointer.task.application;

import com.higor.desafiointer.task.application.command.CreateTaskCommand;
import com.higor.desafiointer.task.application.command.UpdateTaskCommand;
import com.higor.desafiointer.task.application.port.TaskEventPublisher;
import com.higor.desafiointer.task.domain.Task;
import com.higor.desafiointer.task.domain.TaskNotFoundException;
import com.higor.desafiointer.task.domain.TaskRepository;
import com.higor.desafiointer.task.domain.event.TaskCreatedEvent;
import com.higor.desafiointer.task.domain.event.TaskUpdatedEvent;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskEventPublisher taskEventPublisher;

    public TaskService(
            TaskRepository taskRepository,
            TaskEventPublisher taskEventPublisher
    ) {
        this.taskRepository = taskRepository;
        this.taskEventPublisher = taskEventPublisher;
    }

    @Transactional
    public Task create(@NonNull CreateTaskCommand command) {
        Task task = Task.create(command.title(), command.description());

        Task savedTask = taskRepository.save(task);

        taskEventPublisher.publish(new TaskCreatedEvent(
                savedTask.getId(),
                savedTask.getTitle(),
                Instant.now()
        ));

        return savedTask;
    }

    @Transactional(readOnly = true)
    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Task findById(UUID id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }

    @Transactional
    public Task update(UpdateTaskCommand command) {
        Task task = taskRepository.findById(command.id())
                .orElseThrow(() -> new TaskNotFoundException(command.id()));

        task.updateDetails(command.title(), command.description());
        task.changeStatus(command.status());

        Task savedTask = taskRepository.save(task);

        taskEventPublisher.publish(new TaskUpdatedEvent(
                savedTask.getId(),
                savedTask.getTitle(),
                savedTask.getStatus(),
                Instant.now()
        ));

        return savedTask;
    }

    @Transactional
    public void delete(UUID id) {
        if (!taskRepository.existsById(id)) {
            throw new TaskNotFoundException(id);
        }

        taskRepository.deleteById(id);
    }



}