package com.higor.desafiointer.task.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Contrato de persistência do domínio.
 *
 * Esta interface NÃO é Spring Data JPA.
 * Ela representa o que a aplicação precisa para persistir tarefas.
 *
 * A implementação real usando PostgreSQL/JPA ficará na camada infrastructure.
 */
public interface TaskRepository {

    Task save(Task task);

    Optional<Task> findById(UUID id);

    List<Task> findAll();

    boolean existsById(UUID id);

    void deleteById(UUID id);
}