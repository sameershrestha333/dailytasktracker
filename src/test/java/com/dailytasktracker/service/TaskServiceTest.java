package com.dailytasktracker.service;

import com.dailytasktracker.model.Task;
import com.dailytasktracker.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Optional;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    private Task task;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        task = new Task();
        task.setId(1L);
        task.setDescription("Test Task");
    }

    @Test
    void testCreateTask() {
        when(taskRepository.save(any(Task.class))).thenReturn(task);
        Task createdTask = taskService.createTask(task);
        assertNotNull(createdTask);
        assertEquals("Test Task", createdTask.getDescription());
    }

    @Test
    void testGetTaskById() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        Optional<Task> result = taskService.getTaskById(1L);
        assertTrue(result.isPresent());
        assertEquals("Test Task", result.get().getDescription());
    }

    @Test
    void testDeleteTask() {
        taskService.deleteTask(1L);
        verify(taskRepository, times(1)).deleteById(1L);
    }
}
