package com.higor.desafiointer.task.infrastructure.persistence;
import com.higor.desafiointer.task.domain.TaskStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;


@Entity
@Table(name = "tasks")
public class TaskJpaEntity {

    @Id
    private UUID id;

    @Column(nullable = false, length = 120)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TaskStatus status;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    /**
     * Construtor protegido exigido pelo JPA.
     */
    protected TaskJpaEntity() {
    }

    public TaskJpaEntity(
            UUID id,
            String title,
            String description,
            TaskStatus status,
            Instant createdAt
    ) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
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