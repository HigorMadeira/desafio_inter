package com.higor.desafiointer.task.api.dto;

import com.higor.desafiointer.task.domain.Task;
import com.higor.desafiointer.task.domain.TaskStatus;

import java.time.Instant;
import java.util.UUID;


public record TaskResponse(
        UUID id,
        String title,
        String description,
        TaskStatus status,
        Instant createdAt
) {

    public static TaskResponse fromDomain(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getCreatedAt()
        );
    }
}