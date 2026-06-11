package com.higor.desafiointer.task.infrastructure.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.higor.desafiointer.task.application.port.TaskEventPublisher;
import com.higor.desafiointer.task.domain.event.TaskCreatedEvent;
import com.higor.desafiointer.task.domain.event.TaskUpdatedEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import java.util.Map;

@Component
@ConditionalOnProperty(
        name = "app.kafka.enabled",
        havingValue = "true",
        matchIfMissing = true
)
public class SpringTaskEventPublisher implements TaskEventPublisher {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final String topicName;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public SpringTaskEventPublisher(
            KafkaTemplate<String, String> kafkaTemplate,
            @Value("${app.kafka.topics.task-events}") String topicName
    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.topicName = topicName;
    }

    @Override
    public void publish(TaskCreatedEvent event) {
        Map<String, Object> payload = Map.of(
                "eventType", "TASK_CREATED",
                "taskId", event.taskId().toString(),
                "title", event.title(),
                "occurredAt", event.occurredAt().toString()
        );

        send(event.taskId().toString(), payload);
    }

    @Override
    public void publish(TaskUpdatedEvent event) {
        Map<String, Object> payload = Map.of(
                "eventType", "TASK_UPDATED",
                "taskId", event.taskId().toString(),
                "title", event.title(),
                "status", event.status().name(),
                "occurredAt", event.occurredAt().toString()
        );

        send(event.taskId().toString(), payload);
    }

    private void send(String key, Map<String, Object> payload) {
        try {
            String message = objectMapper.writeValueAsString(payload);
            kafkaTemplate.send(topicName, key, message);
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("Could not serialize task event", exception);
        }
    }
}