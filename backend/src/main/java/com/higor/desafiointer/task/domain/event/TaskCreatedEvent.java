package com.higor.desafiointer.task.domain.event;

import java.time.Instant;
import java.util.UUID;


public record TaskCreatedEvent(
        UUID taskId,
        String title,
        Instant occurredAt
) {
}