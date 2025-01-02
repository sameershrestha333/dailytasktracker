/*
package com.dailytasktracker.repository;

import com.dailytasktracker.model.Account;
import com.dailytasktracker.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private AccountRepository accountRepository;

    private Task task;
    private Account account;

    @BeforeEach
    void setUp() {
        // Create and save an Account instance
        account = new Account();
        accountRepository.save(account);

        // Create and save a Task instance
        task = new Task();
        task.setDescription("Test Task");
        task.setAccount(account);
        taskRepository.save(task);
    }

    @Test
    void testCreateTask() {
        Task createdTask = taskRepository.findById(task.getId()).orElse(null);
        assertNotNull(createdTask, "Task should be found");
        assertEquals("Test Task", createdTask.getDescription(), "Description should match");
    }

    @Test
    void testFindAllTasks() {
        Task task2 = new Task();
        task2.setDescription("Second Task");
        task2.setAccount(account);
        taskRepository.save(task2);

        assertEquals(2, taskRepository.findAll().size(), "Should return 2 tasks");
    }

    @Test
    void testFindTaskById() {
        Optional<Task> foundTask = taskRepository.findById(task.getId());
        assertTrue(foundTask.isPresent(), "Task should be found");
        assertEquals("Test Task", foundTask.get().getDescription(), "Description should match");
    }

    @Test
    void testUpdateTask() {
        task.setDescription("Updated Task Description");
        taskRepository.save(task); // Save updated task

        Task updatedTask = taskRepository.findById(task.getId()).orElse(null);
        assertNotNull(updatedTask, "Updated task should be found");
        assertEquals("Updated Task Description", updatedTask.getDescription(), "Description should be updated");
    }

    @Test
    void testDeleteTask() {
        taskRepository.delete(task);

        Optional<Task> deletedTask = taskRepository.findById(task.getId());
        assertFalse(deletedTask.isPresent(), "Task should be deleted");
    }

    @Test
    void testFindByAccount() {
        Task task2 = new Task();
        task2.setDescription("Task for Account");
        task2.setAccount(account);
        taskRepository.save(task2);

        assertEquals(2, taskRepository.findByAccount(account).size(), "Should return 2 tasks for the account");
    }

    @Test
    void testDeleteTaskById() {
        Long taskId = task.getId();
        taskRepository.deleteById(taskId);

        assertFalse(taskRepository.existsById(taskId), "Task should be deleted by ID");
    }
}
*/
