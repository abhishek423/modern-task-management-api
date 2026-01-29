package com.abdev.taskmanager.service.impl;

import com.abdev.taskmanager.entity.Task;
import com.abdev.taskmanager.entity.User;
import com.abdev.taskmanager.entity.enums.TaskStatus;
import com.abdev.taskmanager.exception.ResourceNotFoundException;
import com.abdev.taskmanager.repository.TaskRepository;
import com.abdev.taskmanager.repository.UserRepository;
import com.abdev.taskmanager.service.TaskService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskServiceImpl(TaskRepository taskRepository,
                           UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Task createTask(Task task) {
        //validate parameters
        if (task.getAssignedTo() == null || task.getAssignedTo().getId() == null) {
            throw new IllegalArgumentException("Assigned user is required");
        }
        Long userId = task.getAssignedTo().getId();
        User user = userRepository.findById(userId)
                .orElseThrow(()->
                        new ResourceNotFoundException(
                                "User not found with id: " + userId
                        )
                );
        task.setAssignedTo(user);
        return taskRepository.save(task);
    }

    @Override
    public Task getTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(()->
                        new ResourceNotFoundException(
                                "Task not found with id: " + id
                        ));
    }

    @Override
    public Page<Task> getTaskByStatus(TaskStatus status, Pageable pageable) {
        return taskRepository.findByStatus(status, pageable);
    }

    @Override
    public Page<Task> getTaskByUser(Long userId, Pageable pageable) {
        return taskRepository.findByAssignedTo_Id(userId, pageable);
    }
}
