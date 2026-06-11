package com.higor.desafiointer.task.api.dto;

import com.higor.desafiointer.task.domain.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateTaskRequest(

        @NotBlank(message = "Title is required")
        @Size(max = 120, message = "Title must have at most 120 characters")
        String title,

        String description,

        @NotNull(message = "Status is required")
        TaskStatus status
) {
}