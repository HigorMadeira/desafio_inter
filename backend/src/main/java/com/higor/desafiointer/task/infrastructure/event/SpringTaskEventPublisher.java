package com.higor.desafiointer.task.infrastructure.event;

import com.higor.desafiointer.task.application.port.TaskEventPublisher;
import com.higor.desafiointer.task.domain.event.TaskCreatedEvent;
import com.higor.desafiointer.task.domain.event.TaskUpdatedEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class SpringTaskEventPublisher implements TaskEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public SpringTaskEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void publish(TaskCreatedEvent event) {
        applicationEventPublisher.publishEvent(event);
    }

    @Override
    public void publish(TaskUpdatedEvent event) {
        applicationEventPublisher.publishEvent(event);
    }
}