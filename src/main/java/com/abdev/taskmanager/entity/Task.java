package com.abdev.taskmanager.entity;

import com.abdev.taskmanager.entity.enums.TaskStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "tasks",
        indexes = {
                @Index(name = "idx_task_status", columnList = "status")
        })
@Getter
@Setter
public class Task extends BaseEntity {

    @Column(nullable = false,length = 255)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    private LocalDateTime dueDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_user_id")
    private User assignedTo;
}

