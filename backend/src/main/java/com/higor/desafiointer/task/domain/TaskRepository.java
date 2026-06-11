package com.higor.desafiointer.task.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface TaskRepository {

    Task save(Task task);

    Optional<Task> findById(UUID id);

    List<Task> findAll();

    boolean existsById(UUID id);

    void deleteById(UUID id);
}