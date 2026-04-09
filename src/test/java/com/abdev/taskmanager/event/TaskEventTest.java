package com.abdev.taskmanager.event;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class TaskEventTest {

    @Test
    void shouldCreateEvent() {
        TaskEvent event = TaskEvent.create(1L, TaskEventType.TASK_CREATED);

        assertNotNull(event.getEventId());
        assertEquals(1L,event.getTaskId());
        assertEquals(TaskEventType.TASK_CREATED, event.getEventType());
    }
}
