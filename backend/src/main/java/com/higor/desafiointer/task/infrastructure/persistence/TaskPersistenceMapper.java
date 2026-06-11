package com.higor.desafiointer.task.infrastructure.persistence;

import com.higor.desafiointer.task.domain.Task;

public class TaskPersistenceMapper {

    private TaskPersistenceMapper() {
    }

    public static TaskJpaEntity toJpaEntity(Task task) {
        return new TaskJpaEntity(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getCreatedAt()
        );
    }

    public static Task toDomain(TaskJpaEntity entity) {
        return Task.restore(
                entity.getId(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getStatus(),
                entity.getCreatedAt()
        );
    }
}