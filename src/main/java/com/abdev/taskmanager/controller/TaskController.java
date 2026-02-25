package com.abdev.taskmanager.controller;

import com.abdev.taskmanager.dto.request.CreateTaskRequest;
import com.abdev.taskmanager.dto.response.ApiResponse;
import com.abdev.taskmanager.dto.response.PagedResponse;
import com.abdev.taskmanager.dto.response.TaskResponse;
import com.abdev.taskmanager.entity.Task;
import com.abdev.taskmanager.entity.User;
import com.abdev.taskmanager.entity.enums.TaskStatus;
import com.abdev.taskmanager.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TaskResponse>> createTask(
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

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<TaskResponse>builder()
                        .success(true)
                        .message("Task created successfully")
                        .data(response)
                        .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TaskResponse>> getTask(@PathVariable Long id) {

        Task task = taskService.getTaskById(id);

        TaskResponse response = new TaskResponse();
        response.setId(task.getId());
        response.setTitle(task.getTitle());
        response.setDescription(task.getDescription());
        response.setStatus(task.getStatus());
        response.setDueDate(task.getDueDate());
        response.setAssignedUserId(task.getAssignedTo().getId());

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.<TaskResponse>builder()
                        .success(true)
                        .message("Task fetched successfully")
                        .data(response)
                        .build());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PagedResponse<TaskResponse>>> getTasksByStatus(
            @RequestParam TaskStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        Pageable pageable = PageRequest.of(page,size);
        Page<Task> taskPage = taskService.getTasksByStatus(status, pageable);

        List<TaskResponse> taskResponses = taskPage.getContent()
                .stream().map(task -> {
                    TaskResponse res = new TaskResponse();
                    res.setId(task.getId());
                    res.setTitle(task.getTitle());
                    res.setDescription(task.getDescription());
                    res.setStatus(task.getStatus());
                    res.setDueDate(task.getDueDate());
                    res.setAssignedUserId(task.getAssignedTo().getId());
                    return res;
                })
                .toList();

        PagedResponse<TaskResponse> pagedResponse = new PagedResponse<>(
                taskResponses,
                taskPage.getNumber(),
                taskPage.getSize(),
                taskPage.getTotalElements(),
                taskPage.getTotalPages()
        );

        ApiResponse<PagedResponse<TaskResponse>> response =
                ApiResponse.<PagedResponse<TaskResponse>>builder()
                        .success(true)
                        .message("Tasks fetched successfully")
                        .data(pagedResponse)
                        .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<PagedResponse<TaskResponse>>> getTasksByUserResponse(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page,size);
        Page<Task> taskPage = taskService.getTasksByUser(userId, pageable);

        List<TaskResponse> taskResponses = taskPage.getContent()
                .stream().map(task -> {
                    TaskResponse res = new TaskResponse();
                    res.setId(task.getId());
                    res.setTitle(task.getTitle());
                    res.setDescription(task.getDescription());
                    res.setStatus(task.getStatus());
                    res.setDueDate(task.getDueDate());
                    res.setAssignedUserId(task.getAssignedTo().getId());
                    return res;
                })
                .toList();

        PagedResponse<TaskResponse> pagedResponse = new PagedResponse<>(
                taskResponses,
                taskPage.getNumber(),
                taskPage.getSize(),
                taskPage.getTotalElements(),
                taskPage.getTotalPages()
                );

        ApiResponse<PagedResponse<TaskResponse>> response =
                ApiResponse.<PagedResponse<TaskResponse>>builder()
                        .success(true)
                        .message("Tasks fetched successfully")
                        .data(pagedResponse)
                        .build();

        return ResponseEntity.ok(response);
    }
}
