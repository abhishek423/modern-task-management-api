package com.abdev.taskmanager.kafka;

import com.abdev.taskmanager.event.TaskCreatedEventData;
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

    private static final String TOPIC = "task-events";

    public TaskEventProducer(KafkaTemplate<String, TaskEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendTaskEvent(TaskEvent event) {

        TaskCreatedEventData data = (TaskCreatedEventData) event.getData();
        String key = String.valueOf(data.getTaskId());

        kafkaTemplate.send(TOPIC, key, event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Failed to send event: {}", event, ex);
                    } else {
                        log.info("Event sent successfully: {}", event);
                    }
                });
    }
}
