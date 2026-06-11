package com.higor.desafiointer.task.infrastructure.event;

import com.higor.desafiointer.task.application.port.TaskEventPublisher;
import com.higor.desafiointer.task.domain.event.TaskCreatedEvent;
import com.higor.desafiointer.task.domain.event.TaskUpdatedEvent;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;


@Component
@ConditionalOnProperty(
        name = "app.kafka.enabled",
        havingValue = "false"
)
public class NoOpTaskEventPublisher implements TaskEventPublisher {

    @Override
    public void publish(TaskCreatedEvent event) {
        // Intencionalmente vazio.
    }

    @Override
    public void publish(TaskUpdatedEvent event) {
        // Intencionalmente vazio.
    }
}