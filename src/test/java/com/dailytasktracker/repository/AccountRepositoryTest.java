package com.dailytasktracker.repository;

import com.dailytasktracker.model.Account;
import com.dailytasktracker.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TaskRepository taskRepository;

    private Account account;
    private Task task;

    @BeforeEach
    void setUp() {
        account = new Account();
        accountRepository.save(account);

        task = new Task();
        task.setDescription("Account Task");
        task.setAccount(account);
        taskRepository.save(task);
    }

    @Test
    void testCreateAccount() {
        Account foundAccount = accountRepository.findById(account.getId()).orElse(null);
        assertNotNull(foundAccount);
    }

    @Test
    @Disabled
    void testAddTaskToAccount() {
        List<Task> tasks = accountRepository.findById(account.getId())
                                             .map(Account::getTaskList)
                                             .orElse(null);
        assertNotNull(tasks);
        assertEquals(1, tasks.size());
    }

    @Test
    void testDeleteAccount() {
        accountRepository.delete(account);

        assertFalse(accountRepository.findById(account.getId()).isPresent());
    }

    @Test
    void testFindAllAccounts() {
        Account account2 = new Account();
        accountRepository.save(account2);

        List<Account> accounts = accountRepository.findAll();
        assertEquals(2, accounts.size());
    }
}
