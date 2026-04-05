package com.abdev.taskmanager.event;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class TaskEvent {
    private String eventId = UUID.randomUUID().toString();
    private TaskEventType eventType;
    private Long taskId;
    private LocalDateTime timestamp = LocalDateTime.now();

    public static TaskEvent create(Long taskId, TaskEventType type) {
        TaskEvent event = new TaskEvent();
        event.setTaskId(taskId);
        event.setEventType(type);
        return event;
    }
}
