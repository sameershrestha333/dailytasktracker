package com.dailytasktracker.service;

import com.dailytasktracker.exception.AccountNotFoundException;
import com.dailytasktracker.exception.TaskNotFoundException;
import com.dailytasktracker.model.Account;
import com.dailytasktracker.model.Task;
import com.dailytasktracker.repository.AccountRepository;
import com.dailytasktracker.repository.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    private static final Logger logger = LoggerFactory.getLogger(AccountService.class);

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TaskRepository taskRepository;

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public Optional<Account> getAccountById(Long accountId) {
        return Optional.of(accountRepository.findById(accountId)
                .orElseThrow(() -> {
                    logger.error("Account not found with ID: {}", accountId);
                    return new AccountNotFoundException(accountId);
                }));
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

    public Optional<Account> getAccountWithTasks(Long accountId) {
        // Find the account by ID
        Optional<Account> accountOptional = accountRepository.findById(accountId);

        if (!accountOptional.isPresent()) {
            throw new AccountNotFoundException(accountId);  // Or handle gracefully with custom exception
        }

        // Retrieve the account
        Account account = accountOptional.get();

        // Optionally, we can also manually fetch the tasks if you need additional processing
        account.setTaskList(taskRepository.findByAccountOrderByDateCreatedDesc(account));

        return Optional.of(account);  // Returning the account with its tasks
    }

    @Transactional
    public void removeTasksFromAccount(Long accountId, List<Long> taskIds) {
        logger.info("removeTasksFromAccount : Starting task removal for account ID: {} with task IDs: {}", accountId, taskIds);

        Account account = getAccountById(accountId).get();
        List<Task> tasks = getTasksByIds(taskIds);

        // Remove tasks and save updated account
        removeTasksFromAccountList(account, tasks);
        saveAccount(account);

        // Optionally, delete tasks from DB
        deleteTasks(tasks);
    }



    private void saveAccount(Account account) {
        accountRepository.save(account);
        logger.info("Account with ID: {} saved successfully.", account.getId());
    }

    private void deleteTasks(List<Task> tasks) {
        taskRepository.deleteAll(tasks);
        logger.info("deleteTasks : Deleted tasks with IDs: {}", tasks);
    }

    private List<Task> getTasksByIds(List<Long> taskIds) {
        List<Task> tasks = taskRepository.findAllById(taskIds);
        if (tasks.isEmpty()) {
            logger.error("getTasksByIds : No tasks found for IDs: {}", taskIds);
            throw new TaskNotFoundException(taskIds);
        }
        return tasks;
    }

    private void removeTasksFromAccountList(Account account, List<Task> tasks) {
        account.getTaskList().removeAll(tasks);
        logger.info("removeTasksFromAccount/removeTasksFromAccountList : Removed {} tasks from account ID: {}", tasks.size(), account.getId());
    }

    public Optional<Account> getAccountByEmail(String email) {
        return accountRepository.findByEmail(email); // Assuming you have a method in your repository to find by email
    }
}
