package com.dailytasktracker.controller;

import com.dailytasktracker.model.Account;
import com.dailytasktracker.model.LoginRequest;
import com.dailytasktracker.model.Task;
import com.dailytasktracker.service.AccountService;
import com.dailytasktracker.service.EmailService;
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

import java.util.*;
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

    private EmailService emailService;

    @Autowired
    public AccountController(AccountService accountService, TaskService taskService, PasswordEncoder passwordEncoder, EmailService emailService) {
        this.accountService = accountService;
        this.taskService = taskService;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
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
    public ResponseEntity<Account> getAccountWithTasks(@PathVariable Long accountId) {
        Optional<Account> account = accountService.getAccountWithTasks(accountId);
        if (account.isPresent()) {
            return ResponseEntity.ok(account.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @DeleteMapping("/{accountId}/tasks")
    public ResponseEntity<String>  removeTasksFromAccount(@PathVariable Long accountId, @RequestBody List<Long> taskIds) {
        logger.info("removeTasksFromAccount : Received request to remove tasks with IDs: {} for account ID: {}", taskIds, accountId);

        try {
            accountService.removeTasksFromAccount(accountId, taskIds);
            logger.info("removeTasksFromAccount: Successfully removed tasks with IDs: {} from account ID: {}", taskIds, accountId);
            //return ResponseEntity.ok("Tasks successfully removed from the account.");
            return ResponseEntity.ok("{\"message\": \"Tasks successfully removed from the account.\"}");
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
    public ResponseEntity<Map<String, String>>  login(@RequestBody LoginRequest loginRequest) {
        // Step 1: Retrieve the account based on email
        logger.info("login : Login attempt for email: {}", loginRequest.getEmail());
        Optional<Account> accountOptional = accountService.getAccountByEmail(loginRequest.getEmail());
        Map<String, String> response = new HashMap<>();

        if (!accountOptional.isPresent()) {
            logger.warn("Account with email {} not found", loginRequest.getEmail());
            response.put("message", "Account not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        logger.info("Account with email {} found. Proceeding to password verification", loginRequest.getEmail());
        Account account = accountOptional.get();

        // Step 2: Compare the password using BCryptPasswordEncoder
        boolean isPasswordMatch = passwordEncoder.matches(loginRequest.getPassword(), account.getPassword());
        if (isPasswordMatch) {
            logger.info("Login successful for email: {}", loginRequest.getEmail());
            // Login successful, you can return a success message or JWT token (if using JWT for authentication)
            response.put("message", "Login successful");
            response.put("accountId", String.valueOf(account.getId()));
            return ResponseEntity.ok(response);
        } else {
            logger.warn("Invalid password attempt for email: {}", loginRequest.getEmail());
            response.put("message", "Invalid credentials");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Map<String, String>> forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        logger.info("Forgot password request received for email: {}", email);

        Optional<Account> accountOptional = accountService.getAccountByEmail(email);
        Map<String, String> response = new HashMap<>();

        if (!accountOptional.isPresent()) {
            response.put("message", "If your email is registered, a password reset link has been sent.");
            return ResponseEntity.ok(response); // Return a generic response to prevent email enumeration
        }

        // Generate a reset token
        String resetToken = UUID.randomUUID().toString();
        Account account = accountOptional.get();
        account.setResetToken(resetToken);
        accountService.updateAccount(account.getId(), account);

        // Construct reset password link
        String resetLink = "http://localhost:8080/resetPassword.html?token=" + resetToken;

        // Send email
        String subject = "Password Reset Request";
        String body = "Hi " + account.getFirstName() + ",\n\n" +
                "You requested to reset your password. Click the link below to reset it:\n" +
                resetLink + "\n\n" +
                "If you did not request this, please ignore this email.\n\n" +
                "Thanks,\nDaily Task Tracker Team";

        emailService.sendEmail(email, subject, body);
        logger.info("Password reset email sent to: {}", email);

        response.put("message", "If your email is registered, a password reset link has been sent.");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, String>> resetPassword(@RequestBody Map<String, String> request) {
        String resetToken = request.get("resetToken");
        String newPassword = request.get("newPassword");

        Optional<Account> accountOptional = accountService.getAccountByResetToken(resetToken);
        Map<String, String> response = new HashMap<>();

        if (!accountOptional.isPresent()) {
            response.put("message", "Invalid or expired reset token.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        Account account = accountOptional.get();
        account.setPassword(passwordEncoder.encode(newPassword));
        account.setResetToken(null); // Clear the token after reset
        accountService.updateAccount(account.getId(), account);

        response.put("message", "Password has been reset successfully.");
        return ResponseEntity.ok(response);
    }
}
