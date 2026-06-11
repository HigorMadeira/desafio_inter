package com.higor.desafiointer.task.infrastructure.event;

import com.higor.desafiointer.task.domain.event.TaskCreatedEvent;
import com.higor.desafiointer.task.domain.event.TaskUpdatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class LogTaskEventHandler {

    private static final Logger logger = LoggerFactory.getLogger(LogTaskEventHandler.class);

    @EventListener
    public void handle(TaskCreatedEvent event) {
        logger.info(
                "task_event type=TASK_CREATED taskId={} title=\"{}\" occurredAt={}",
                event.taskId(),
                event.title(),
                event.occurredAt()
        );
    }

    @EventListener
    public void handle(TaskUpdatedEvent event) {
        logger.info(
                "task_event type=TASK_UPDATED taskId={} title=\"{}\" status={} occurredAt={}",
                event.taskId(),
                event.title(),
                event.status(),
                event.occurredAt()
        );
    }
}