package com.higor.desafiointer.task.application.command;

import com.higor.desafiointer.task.domain.TaskStatus;

import java.util.UUID;

public record UpdateTaskCommand(
        UUID id,
        String title,
        String description,
        TaskStatus status
) {
}