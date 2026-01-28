package com.abdev.taskmanager.repository;

import com.abdev.taskmanager.entity.Task;
import com.abdev.taskmanager.entity.enums.TaskStatus;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TaskRepository extends JpaRepository<Task, Long> {

    Page<Task> findByStatus(TaskStatus status, Pageable pageable);

    Page<Task> findByAssignedTo_Id(Long userId, Pageable pageable);
}
