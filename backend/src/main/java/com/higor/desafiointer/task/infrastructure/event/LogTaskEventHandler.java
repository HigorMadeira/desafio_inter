package com.higor.desafiointer.task.infrastructure.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class LogTaskEventHandler {

    private static final Logger logger = LoggerFactory.getLogger(LogTaskEventHandler.class);

    @KafkaListener(topics = "${app.kafka.topics.task-events}")
    public void handle(String payload) {
        logger.info("task_event source=kafka payload={}", payload);
    }
}