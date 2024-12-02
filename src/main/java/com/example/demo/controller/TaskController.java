package com.example.demo.controller;

import com.example.demo.model.Task;
import com.example.demo.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    // List all tasks
    @GetMapping
    public List<Task> getAllTasks() {
        return taskService.getAllTasks();
    }

    // List tasks by owner
    @GetMapping("/user/{userId}")
    public List<Task> getTasksByUser(@PathVariable Long userId) {
        return taskService.getTasksByUser(userId);
    }

    // Get a task by ID
    @GetMapping("/{taskId}")
    public Optional<Task> getTaskById(@PathVariable Long taskId) {
        return taskService.getTaskById(taskId);
    }

    // Update task's estimated hours
    @PutMapping("/{taskId}/estimatedHours")
    public Task updateTaskEstimatedHours(@PathVariable Long taskId, @RequestParam Integer newEstimatedHours) {
        return taskService.updateEstimatedHours(taskId, newEstimatedHours); // Updated method name
    }

    // Remove a task
    @DeleteMapping("/{taskId}")
    public void deleteTask(@PathVariable Long taskId) {
        taskService.deleteTask(taskId);
    }

    // Create a new task
    @PostMapping
    public ResponseEntity<?> createTask(@RequestBody Task task) {
        try {
            // Calls TaskService to create the task
            Task createdTask = taskService.createTask(task);
            return ResponseEntity.ok(createdTask);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}
