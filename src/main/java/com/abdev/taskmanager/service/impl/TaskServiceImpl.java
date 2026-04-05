package com.abdev.taskmanager.service.impl;

import com.abdev.taskmanager.entity.Task;
import com.abdev.taskmanager.entity.User;
import com.abdev.taskmanager.entity.enums.TaskStatus;
import com.abdev.taskmanager.event.TaskEvent;
import com.abdev.taskmanager.event.TaskEventType;
import com.abdev.taskmanager.exception.ResourceNotFoundException;
import com.abdev.taskmanager.kafka.TaskEventProducer;
import com.abdev.taskmanager.repository.TaskRepository;
import com.abdev.taskmanager.repository.UserRepository;
import com.abdev.taskmanager.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TaskServiceImpl implements TaskService {

    private static final Logger log =
            LoggerFactory.getLogger(TaskServiceImpl.class);
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TaskEventProducer taskEventProducer;

    public TaskServiceImpl(TaskRepository taskRepository,
                           UserRepository userRepository,
                           TaskEventProducer taskEventProducer) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.taskEventProducer = taskEventProducer;
    }

    @Override
    public Task createTask(Task task) {

        //validate parameters
        if (task.getAssignedTo() == null || task.getAssignedTo().getId() == null) {
            log.warn("Task creation failed: assigned user is missing");
            throw new IllegalArgumentException("Assigned user is required");
        }

        Long userId = task.getAssignedTo().getId();
        log.info("Creating task '{}' for user id: {}", task.getTitle(), userId);

        User user = userRepository.findById(userId)
                .orElseThrow(()-> {

                    log.warn("Assigned user not found with id: {}", userId);

                    return new ResourceNotFoundException(
                            "User not found with id: " + userId
                    );
                });

        task.setAssignedTo(user);

        Task savedTask = taskRepository.save(task);
        log.info("Task created successfully with id: {}", savedTask.getId());

        TaskEvent event = TaskEvent.create(
                savedTask.getId(),
                TaskEventType.TASK_CREATED
        );

        taskEventProducer.sendTaskEvent(event);
        log.info("Kafka event sent: taskId={}, eventType={}, eventTimestamp={}",
                savedTask.getId(),
                event.getEventType(),
                event.getTimestamp());

        return savedTask;
    }

    @Override
    public Task getTaskById(Long id) {

        log.debug("Fetching task by id: {}", id);

        return taskRepository.findById(id)
                .orElseThrow(() -> {

                    log.warn("Task not found with id: {}", id);

                    return new ResourceNotFoundException(
                            "Task not found with id: " + id
                    );
                });
    }

    @Override
    public Page<Task> getTasksByStatus(TaskStatus status, Pageable pageable) {

        log.debug("Fetching tasks by status: {}, page: {}, size: {}",
                status,
                pageable.getPageNumber(),
                pageable.getPageSize());

        return taskRepository.findByStatus(status, pageable);
    }

    @Override
    public Page<Task> getTasksByUser(Long userId, Pageable pageable) {

        log.debug("Fetching tasks for user id: {}, page: {}, size: {}",
                userId,
                pageable.getPageNumber(),
                pageable.getPageSize());

        return taskRepository.findByAssignedTo_Id(userId, pageable);
    }
}
