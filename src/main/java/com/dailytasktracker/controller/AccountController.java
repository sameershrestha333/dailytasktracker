package com.dailytasktracker.controller;

import com.dailytasktracker.model.Account;
import com.dailytasktracker.model.LoginRequest;
import com.dailytasktracker.model.Task;
import com.dailytasktracker.service.AccountService;
import com.dailytasktracker.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
@Validated
public class AccountController {

    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

    private AccountService accountService;

    private TaskService taskService;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AccountController(AccountService accountService, TaskService taskService, PasswordEncoder passwordEncoder) {
        this.accountService = accountService;
        this.taskService = taskService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public List<Account> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccountById(@PathVariable Long id) {
        Optional<Account> account = accountService.getAccountById(id);
        return account.map(ResponseEntity::ok)
                      .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    public ResponseEntity<Account> createAccount(@RequestBody Account account) {
        String encodedPassword = passwordEncoder.encode(account.getPassword());
        account.setPassword(encodedPassword);

        Account createdAccount = accountService.createAccount(account);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAccount);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Account> updateAccount(@PathVariable Long id, @RequestBody Account account) {
        Account updatedAccount = accountService.updateAccount(id, account);
        return updatedAccount != null ? ResponseEntity.ok(updatedAccount)
                                      : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {
        accountService.deleteAccount(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{accountId}/tasks")
    public Account getAccountWithTasks(@PathVariable Long accountId) {
        return accountService.getAccountWithTasks(accountId).get();
    }

    @DeleteMapping("/{accountId}/tasks")
    public ResponseEntity<String>  removeTasksFromAccount(@PathVariable Long accountId, @RequestBody List<Long> taskIds) {
        logger.info("removeTasksFromAccount : Received request to remove tasks with IDs: {} for account ID: {}", taskIds, accountId);

        try {
            accountService.removeTasksFromAccount(accountId, taskIds);
            logger.info("removeTasksFromAccount: Successfully removed tasks with IDs: {} from account ID: {}", taskIds, accountId);
            return ResponseEntity.ok("Tasks successfully removed from the account.");
        } catch (Exception e) {
            logger.error("removeTasksFromAccount : Error while removing tasks with IDs: {} from account ID: {}. Error: {}", taskIds, accountId, e.getMessage(), e);
            return ResponseEntity.status(500).body("Error while removing tasks: " + e.getMessage());
        }
    }

    @PostMapping("/{accountId}/tasks")
    public ResponseEntity<?> createTasksForAccount(
            @PathVariable Long accountId,
            @RequestBody List<String> taskDescriptions) {

        // Find the Account by ID
        Optional<Account> account = accountService.getAccountById(accountId);
        if (!account.isPresent()) {
            return new ResponseEntity<>("Account not found", HttpStatus.NOT_FOUND);
        }

        // Create tasks and associate them with the Account
        List<Task> tasks = taskDescriptions.stream()
                .map(description -> new Task(description, account.get()))  // Create new Task with description and associated account
                .collect(Collectors.toList());

        // Save tasks in the database
        List<Task> createdTasks = taskService.saveTasks(tasks);

        return new ResponseEntity<>(createdTasks, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        // Step 1: Retrieve the account based on email
        logger.info("login : Login attempt for email: {}", loginRequest.getEmail());
        Optional<Account> accountOptional = accountService.getAccountByEmail(loginRequest.getEmail());

        if (!accountOptional.isPresent()) {
            logger.warn("Account with email {} not found", loginRequest.getEmail());
            return new ResponseEntity<>("Account not found", HttpStatus.NOT_FOUND);
        }
        logger.info("Account with email {} found. Proceeding to password verification", loginRequest.getEmail());
        Account account = accountOptional.get();

        // Step 2: Compare the password using BCryptPasswordEncoder
        boolean isPasswordMatch = passwordEncoder.matches(loginRequest.getPassword(), account.getPassword());

        if (isPasswordMatch) {
            logger.info("Login successful for email: {}", loginRequest.getEmail());
            // Login successful, you can return a success message or JWT token (if using JWT for authentication)
            return ResponseEntity.ok("Login successful");
        } else {
            logger.warn("Invalid password attempt for email: {}", loginRequest.getEmail());
            return new ResponseEntity<>("Invalid password", HttpStatus.FORBIDDEN);
        }
    }
}
