package com.dailytasktracker.controller;

import com.dailytasktracker.model.Account;
import com.dailytasktracker.model.Task;
import com.dailytasktracker.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

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
        return accountService.getAccountWithTasks(accountId);
    }

    @DeleteMapping("/{accountId}/tasks")
    public void removeTasksFromAccount(@PathVariable Long accountId, @RequestBody List<Long> taskIds) {
        accountService.removeTasksFromAccount(accountId, taskIds);
    }
}
