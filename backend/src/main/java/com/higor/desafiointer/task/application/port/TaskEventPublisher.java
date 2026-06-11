package com.higor.desafiointer.task.application.port;

import com.higor.desafiointer.task.domain.event.TaskCreatedEvent;
import com.higor.desafiointer.task.domain.event.TaskUpdatedEvent;

public interface TaskEventPublisher {

    void publish(TaskCreatedEvent event);

    void publish(TaskUpdatedEvent event);
}