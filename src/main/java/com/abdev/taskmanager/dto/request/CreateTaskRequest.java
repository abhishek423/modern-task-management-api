package com.abdev.taskmanager.dto.request;

import com.abdev.taskmanager.entity.enums.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CreateTaskRequest {

    @NotBlank
    private String title;

    private String description;

    private TaskStatus status;

    private LocalDateTime dueDate;

    @NotNull
    private Long assignedUserId;
}
