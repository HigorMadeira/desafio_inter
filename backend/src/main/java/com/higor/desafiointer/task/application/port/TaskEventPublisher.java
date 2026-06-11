package com.higor.desafiointer.task.application.port;

import com.higor.desafiointer.task.domain.event.TaskCreatedEvent;
import com.higor.desafiointer.task.domain.event.TaskUpdatedEvent;

/**
 * Porta de saída para publicação de eventos.
 *
 * A aplicação sabe que precisa publicar eventos,
 * mas não sabe COMO isso será feito.
 *
 * A implementação concreta ficará na infrastructure:
 * pode ser log, Spring Event, Kafka, Redpanda etc.
 */
public interface TaskEventPublisher {

    void publish(TaskCreatedEvent event);

    void publish(TaskUpdatedEvent event);
}