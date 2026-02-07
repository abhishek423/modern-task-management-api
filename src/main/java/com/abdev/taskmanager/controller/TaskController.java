package com.abdev.taskmanager.controller;

import com.abdev.taskmanager.dto.request.CreateTaskRequest;
import com.abdev.taskmanager.dto.response.TaskResponse;
import com.abdev.taskmanager.entity.Task;
import com.abdev.taskmanager.entity.User;
import com.abdev.taskmanager.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    public ResponseEntity<TaskResponse> createTask(
            @Valid @RequestBody CreateTaskRequest request
            ) {

        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus());
        task.setDueDate(request.getDueDate());

        User user = new User();
        user.setId(request.getAssignedUserId());
        task.setAssignedTo(user);

        Task saved = taskService.createTask(task);

        TaskResponse response = new TaskResponse();
        response.setId(saved.getId());
        response.setTitle(saved.getTitle());
        response.setDescription(saved.getDescription());
        response.setStatus(saved.getStatus());
        response.setDueDate(saved.getDueDate());
        response.setAssignedUserId(saved.getAssignedTo().getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTask(@PathVariable Long id) {

        Task task = taskService.getTaskById(id);

        TaskResponse response = new TaskResponse();
        response.setId(task.getId());
        response.setTitle(task.getTitle());
        response.setDescription(task.getDescription());
        response.setStatus(task.getStatus());
        response.setDueDate(task.getDueDate());
        response.setAssignedUserId(task.getAssignedTo().getId());

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
