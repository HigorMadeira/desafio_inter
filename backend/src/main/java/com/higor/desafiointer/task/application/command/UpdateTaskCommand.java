package com.higor.desafiointer.task.application.command;

import com.higor.desafiointer.task.domain.TaskStatus;

import java.util.UUID;

/**
 * Comando de aplicação para atualização de tarefa.
 *
 * Contém apenas os dados necessários para o caso de uso.
 */
public record UpdateTaskCommand(
        UUID id,
        String title,
        String description,
        TaskStatus status
) {
}