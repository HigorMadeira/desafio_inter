package com.higor.desafiointer.task.application.command;

public record CreateTaskCommand(
        String title,
        String description
) {
}