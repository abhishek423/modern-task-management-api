package com.abdev.taskmanager.service;

import com.abdev.taskmanager.entity.Task;
import com.abdev.taskmanager.entity.User;
import com.abdev.taskmanager.entity.enums.TaskStatus;
import com.abdev.taskmanager.exception.ResourceNotFoundException;
import com.abdev.taskmanager.kafka.TaskEventProducer;
import com.abdev.taskmanager.repository.TaskRepository;
import com.abdev.taskmanager.repository.UserRepository;
import com.abdev.taskmanager.service.impl.TaskServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskEventProducer taskEventProducer;

    @InjectMocks
    private TaskServiceImpl taskService;

    @Test
    void shouldCreateTaskSuccessfully() {

        User user = new User();
        user.setId(1L);

        Task task = new Task();
        task.setAssignedTo(user);
        task.setTitle("Task Title");
        task.setStatus(TaskStatus.TODO);
        task.setDescription("Task Desc");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(taskRepository.save(task)).thenReturn(task);

        Task saved = taskService.createTask(task);

        assertNotNull(saved);
        assertEquals("Task Title",saved.getTitle());
        verify(taskRepository, times(1)).save(task);
        verify(taskEventProducer).sendTaskEvent(any());
    }

    @Test
    void shouldThrowExceptionWhenAssignedUserIsMissing() {

        Task task = new Task();

        assertThrows(IllegalArgumentException.class, () -> taskService.createTask(task));
    }

    @Test
    void shouldThrowExceptionWhenTaskNotFound() {

        Task task = new Task();
        task.setId(1L);

        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> taskService.getTaskById(1L));
    }
}
