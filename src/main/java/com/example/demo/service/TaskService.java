package com.example.demo.service;

import com.example.demo.model.Task;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    // List all tasks
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    // List tasks assigned to a specific owner
    public List<Task> getTasksByUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("User with id " + userId + " does not exist");
        }
        return taskRepository.findByUserId(userId);
    }

    // Get task by ID
    public Optional<Task> getTaskById(Long taskId) {
        return taskRepository.findById(taskId);
    }

    // Update remaining effort (estimated hours) of a task
    @Transactional
    public Task updateEstimatedHours(Long taskId, Integer newEstimatedHours) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found with id: " + taskId));
        task.setEstimatedHours(newEstimatedHours);
        return taskRepository.save(task);
    }

    // Create a new task
    @Transactional
    public Task createTask(Task task) {
        logger.info("Creating new task: {}", task);

        if (task.getEstimatedHours() != null && task.getEstimatedHours() < 0) {
            throw new IllegalArgumentException("Estimated hours cannot be negative");
        }

        if (task.getCompletedHours() != null && task.getCompletedHours() < 0) {
            throw new IllegalArgumentException("Completed hours cannot be negative");
        }

        if (task.getUser() == null || !userRepository.existsById(task.getUser().getId())) {
            throw new IllegalArgumentException("Invalid owner: the owner must exist");
        }

        return taskRepository.save(task);
    }

    // Remove a task by ID
    @Transactional
    public void deleteTask(Long taskId) {
        if (!taskRepository.existsById(taskId)) {
            throw new IllegalArgumentException("Task with id " + taskId + " does not exist");
        }
        taskRepository.deleteById(taskId);
        logger.info("Deleted task with id {}", taskId);
    }
}
