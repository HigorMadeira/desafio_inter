package com.higor.desafiointer.task.domain.event;

import com.higor.desafiointer.task.domain.TaskStatus;

import java.time.Instant;
import java.util.UUID;


public record TaskUpdatedEvent(
        UUID taskId,
        String title,
        TaskStatus status,
        Instant occurredAt
) {
}