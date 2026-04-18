package com.abdev.taskmanager.event;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class TaskEventTest {

    @Test
    void shouldCreateEvent() {
        TaskEvent event = TaskEvent.create(1L, TaskEventType.TASK_CREATED);

        assertNotNull(event.getEventId());
        assertEquals(TaskEventType.TASK_CREATED, event.getEventType());
        assertEquals("v1",event.getEventVersion());

        TaskCreatedEventData data = (TaskCreatedEventData) event.getData();
        assertNotNull(data);
        assertEquals(1L,data.getTaskId());

    }
}
