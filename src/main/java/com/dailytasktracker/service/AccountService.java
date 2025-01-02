package com.dailytasktracker.service;

import com.dailytasktracker.model.Account;
import com.dailytasktracker.model.Task;
import com.dailytasktracker.repository.AccountRepository;
import com.dailytasktracker.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TaskRepository taskRepository;

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public Optional<Account> getAccountById(Long id) {
        return accountRepository.findById(id);
    }

    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }

    public Account updateAccount(Long id, Account account) {
        if (accountRepository.existsById(id)) {
            account.setId(id);
            return accountRepository.save(account);
        }
        return null;
    }

    public void deleteAccount(Long id) {
        accountRepository.deleteById(id);
    }

    public Account getAccountWithTasks(Long accountId) {
        // Find the account by ID
        Optional<Account> accountOptional = accountRepository.findById(accountId);

        if (!accountOptional.isPresent()) {
            throw new RuntimeException("Account not found for id: " + accountId);  // Or handle gracefully with custom exception
        }

        // Retrieve the account
        Account account = accountOptional.get();

        // Optionally, we can also manually fetch the tasks if you need additional processing
        account.setTaskList(taskRepository.findByAccount(account));

        return account;  // Returning the account with its tasks
    }

    public void removeTasksFromAccount(Long accountId, List<Long> taskIds) {
        // Retrieve the account by its ID
        Optional<Account> accountOptional = accountRepository.findById(accountId);

        if (!accountOptional.isPresent()) {
            throw new RuntimeException("Account not found for id: " + accountId);  // Or handle gracefully
        }

        Account account = accountOptional.get();

        // Remove tasks from the account by their task IDs
        List<Task> tasksToRemove = taskRepository.findAllById(taskIds);

        // Remove tasks from the account's task list
        account.getTaskList().removeAll(tasksToRemove);

        // Save the updated account
        accountRepository.save(account);

        // Optionally, delete tasks from the database
        taskRepository.deleteAll(tasksToRemove);
    }
}
