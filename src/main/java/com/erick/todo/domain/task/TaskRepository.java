package com.erick.todo.domain.task;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
	Task findById(long id);
	List<Task> findByTitleContaining(String title, Sort sort);
    List<Task> findByDescriptionContaining(String description, Sort sort);
    List<Task> findByCompleted(boolean completed, Sort sort);
}

