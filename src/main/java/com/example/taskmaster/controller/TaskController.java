package com.example.taskmaster.controller;

import com.example.taskmaster.model.Task;
import com.example.taskmaster.model.User;
import com.example.taskmaster.service.TaskService;
import com.example.taskmaster.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    private final TaskService taskService;
    private final UserService userService;
    public TaskController(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<Task> createTask(@RequestParam String username,
                                           @RequestParam String title,
                                           @RequestParam String description,
                                           @RequestParam(required=false) String dueDate) {
        Optional<User> userOpt = userService.findByUsername(username);
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        LocalDateTime date = dueDate != null ? LocalDateTime.parse(dueDate) : null;
        Task created = taskService.createTask(userOpt.get(), title, description, date);
        return ResponseEntity.ok(created);
    }

    @GetMapping
    public ResponseEntity<List<Task>> getUserTasks(@RequestParam Long userId) {
        return ResponseEntity.ok(taskService.getTasksForUser(userId));
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<Task> updateTask(@PathVariable Long taskId,
                                           @RequestParam String title,
                                           @RequestParam String description,
                                           @RequestParam String dueDate,
                                           @RequestParam boolean completed) {
        LocalDateTime date = LocalDateTime.parse(dueDate);
        Task updated = taskService.updateTask(taskId, title, description, date, completed);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.noContent().build();
    }
}
