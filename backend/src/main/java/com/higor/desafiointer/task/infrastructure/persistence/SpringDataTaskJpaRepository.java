package com.higor.desafiointer.task.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpringDataTaskJpaRepository extends JpaRepository<TaskJpaEntity, UUID> {
}