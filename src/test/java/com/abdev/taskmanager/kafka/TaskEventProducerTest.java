package com.abdev.taskmanager.kafka;

import com.abdev.taskmanager.event.TaskEvent;
import com.abdev.taskmanager.event.TaskEventType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.concurrent.CompletableFuture;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskEventProducerTest {

    @Mock
    private KafkaTemplate<String, TaskEvent> kafkaTemplate;

    @InjectMocks
    TaskEventProducer taskEventProducer;

    @Test
    void shouldSendEvent() {
        TaskEvent event = TaskEvent.create(1L, TaskEventType.TASK_CREATED);

        when(kafkaTemplate.send(anyString(), anyString(), any()))
                .thenReturn(CompletableFuture.completedFuture(null));

        taskEventProducer.sendTaskEvent(event);
        verify(kafkaTemplate).send(eq("task-events"),anyString(),eq(event));
    }

}
