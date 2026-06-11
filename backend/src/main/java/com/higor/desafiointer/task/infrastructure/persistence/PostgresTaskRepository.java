package com.higor.desafiointer.task.infrastructure.persistence;

import com.higor.desafiointer.task.domain.Task;
import com.higor.desafiointer.task.domain.TaskRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository
public class PostgresTaskRepository implements TaskRepository {

    private final SpringDataTaskJpaRepository repository;

    public PostgresTaskRepository(SpringDataTaskJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Task save(Task task) {
        TaskJpaEntity entity = TaskPersistenceMapper.toJpaEntity(task);
        TaskJpaEntity savedEntity = repository.save(entity);

        return TaskPersistenceMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Task> findById(UUID id) {
        return repository.findById(id)
                .map(TaskPersistenceMapper::toDomain);
    }

    @Override
    public List<Task> findAll() {
        return repository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"))
                .stream()
                .map(TaskPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public boolean existsById(UUID id) {
        return repository.existsById(id);
    }

    @Override
    public void deleteById(UUID id) {
        repository.deleteById(id);
    }
}