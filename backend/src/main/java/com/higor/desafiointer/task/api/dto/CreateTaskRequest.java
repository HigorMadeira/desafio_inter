package com.higor.desafiointer.task.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateTaskRequest(

        @NotBlank(message = "Title is required")
        @Size(max = 120, message = "Title must have at most 120 characters")
        String title,

        String description
) {
}