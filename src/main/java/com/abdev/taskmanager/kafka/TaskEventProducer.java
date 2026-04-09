package com.abdev.taskmanager.kafka;

import com.abdev.taskmanager.event.TaskEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class TaskEventProducer {
    private final KafkaTemplate<String, TaskEvent> kafkaTemplate;

    private static final Logger log =
            LoggerFactory.getLogger(TaskEventProducer.class);

    public TaskEventProducer(KafkaTemplate<String, TaskEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendTaskEvent(TaskEvent event) {
        kafkaTemplate.send("task-events", event.getTaskId().toString(), event)
                .whenComplete((result, ex) -> {

                    if (ex != null) {
                        log.error("Failed to send event: {}", event, ex);
                    } else {
                        log.info("Event sent successfully: {}", event);
                    }
                });
    }
}
