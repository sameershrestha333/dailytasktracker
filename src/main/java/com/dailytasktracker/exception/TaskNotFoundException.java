package com.dailytasktracker.exception;

import java.util.List;

public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException(List<Long> taskIds) {
        super("No tasks found for IDs: " + taskIds);
    }
}