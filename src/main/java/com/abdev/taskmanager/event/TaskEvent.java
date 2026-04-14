package com.abdev.taskmanager.event;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class TaskEvent {
    private String eventId = UUID.randomUUID().toString();
    private TaskEventType eventType;
    private String eventVersion;
    private LocalDateTime timestamp = LocalDateTime.now();
    private Object data;

    public static TaskEvent create(Long taskId, TaskEventType type) {
        TaskEvent event = new TaskEvent();

        TaskCreatedEventData taskCreatedEventData = new TaskCreatedEventData();
        taskCreatedEventData.setTaskId(taskId);

        event.setEventType(type);
        event.setEventVersion("v1");
        event.setData(taskCreatedEventData);

        return event;
    }
}
