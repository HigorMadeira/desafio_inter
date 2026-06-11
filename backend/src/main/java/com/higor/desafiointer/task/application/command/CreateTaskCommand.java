package com.higor.desafiointer.task.application.command;

/**
 * Comando de aplicação para criação de tarefa.
 *
 * Ele representa a intenção de criar uma tarefa,
 * sem estar preso a HTTP, JSON ou Controller.
 */
public record CreateTaskCommand(
        String title,
        String description
) {
}