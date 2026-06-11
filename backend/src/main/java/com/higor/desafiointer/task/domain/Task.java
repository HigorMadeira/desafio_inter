package com.higor.desafiointer.task.domain;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class Task {

    private static final int MAX_TITLE_LENGTH = 120;

    private final UUID id;
    private String title;
    private String description;
    private TaskStatus status;
    private final Instant createdAt;

    private Task(
            UUID id,
            String title,
            String description,
            TaskStatus status,
            Instant createdAt
    ) {
        this.id = Objects.requireNonNull(id, "id is required");
        this.title = validateTitle(title);
        this.description = description;
        this.status = Objects.requireNonNull(status, "status is required");
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt is required");
    }


    public static Task create(String title, String description) {
        return new Task(
                UUID.randomUUID(),
                title,
                description,
                TaskStatus.PENDING,
                Instant.now()
        );
    }

    /**
     * Factory method para reconstruir uma Task vinda do banco.
     *
     * Isso será usado depois pela camada de infraestrutura,
     * quando convertermos TaskJpaEntity -> Task.
     */
    public static Task restore(
            UUID id,
            String title,
            String description,
            TaskStatus status,
            Instant createdAt
    ) {
        return new Task(id, title, description, status, createdAt);
    }

    /**
     * Atualiza os dados editáveis da tarefa.
     *
     * O id e createdAt não mudam.
     */
    public void updateDetails(String title, String description) {
        this.title = validateTitle(title);
        this.description = description;
    }

    /**
     * Altera o status da tarefa.
     */
    public void changeStatus(TaskStatus status) {
        this.status = Objects.requireNonNull(status, "status is required");
    }

    private static String validateTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Task title is required");
        }

        String normalizedTitle = title.trim();

        if (normalizedTitle.length() > MAX_TITLE_LENGTH) {
            throw new IllegalArgumentException("Task title must have at most 120 characters");
        }

        return normalizedTitle;
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}