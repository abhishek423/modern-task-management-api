package com.abdev.taskmanager.service;

import com.abdev.taskmanager.entity.Task;
import com.abdev.taskmanager.entity.enums.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TaskService {

    Task createTask(Task task);

    Task getTaskById(Long id);

    Page<Task> getTaskByStatus(TaskStatus status, Pageable pageable);

    Page<Task> getTaskByUser(Long userId, Pageable pageable);
}
