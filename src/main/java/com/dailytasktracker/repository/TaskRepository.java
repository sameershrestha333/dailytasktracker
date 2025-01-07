package com.dailytasktracker.repository;

import com.dailytasktracker.model.Account;
import com.dailytasktracker.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByAccountOrderByDateCreatedDesc(Account account);
}
