package com.erick.todo.domain.task;

import java.time.LocalDate;

public record RequestTask(String title, String description, Boolean completed, Priority priority, LocalDate overdue) {
}
