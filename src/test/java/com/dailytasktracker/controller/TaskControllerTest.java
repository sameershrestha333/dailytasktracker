package com.dailytasktracker.controller;

import com.dailytasktracker.model.Task;
import com.dailytasktracker.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class TaskControllerTest {

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController taskController;

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
        when(taskService.createTask(any(Task.class))).thenReturn(task);
        ResponseEntity<Task> response = taskController.createTask(task);
        assertEquals(201, response.getStatusCodeValue());
        assertEquals("Test Task", response.getBody().getDescription());
    }

    @Test
    void testGetTaskById() {
        when(taskService.getTaskById(1L)).thenReturn(Optional.of(task));
        ResponseEntity<Task> response = taskController.getTaskById(1L);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Test Task", response.getBody().getDescription());
    }

    @Test
    void testDeleteTask() {
        doNothing().when(taskService).deleteTask(1L);
        ResponseEntity<Void> response = taskController.deleteTask(1L);
        assertEquals(204, response.getStatusCodeValue());
    }
}
