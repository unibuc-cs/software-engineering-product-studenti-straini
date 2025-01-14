package com.example.taskmaster.controller;

import com.example.taskmaster.model.Category;
import com.example.taskmaster.model.Task;
import com.example.taskmaster.model.User;
import com.example.taskmaster.service.CategoryService;
import com.example.taskmaster.service.NotificationService;
import com.example.taskmaster.service.TaskService;
import com.example.taskmaster.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    private final TaskService taskService;
    private final UserService userService;
    private final NotificationService notificationService;
    private final CategoryService categoryService;
    public TaskController(TaskService taskService, UserService userService, NotificationService notificationService, CategoryService categoryService) {
        this.taskService = taskService;
        this.userService = userService;
        this.notificationService = notificationService;
        this.categoryService = categoryService;
    }

    @PostMapping("/test-email")
    public ResponseEntity<String> testEmail() {
        notificationService.sendEmail("test-recipient@example.com", "Test Email", "This is a test email.");
        return ResponseEntity.ok("Test email sent successfully!");
    }
    @PostMapping("/create")
    public ResponseEntity<Task> createTask(@RequestParam String username,
                                           @RequestParam String title,
                                           @RequestParam String description,
                                           @RequestParam(required = false) String dueDate,
                                           @RequestParam(required = false) String categoryName) {
        Optional<User> userOpt = userService.findByUsername(username);
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        LocalDateTime date = null;
        if (dueDate != null) {
            date = LocalDateTime.parse(dueDate, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        }

        Category category = null;
        if (categoryName != null) {
            category = categoryService.getCategoryByName(categoryName)
                    .orElseGet(() -> categoryService.createCategory(categoryName));
        }

        Task task = new Task();
        task.setUser(userOpt.get());
        task.setTitle(title);
        task.setDescription(description);
        task.setDueDate(date);
        task.setCategory(category);

        return ResponseEntity.ok(taskService.save(task));
    }





    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestParam String username,
                                      @RequestParam String password,
                                      @RequestParam(required = false) String email) {
        if (email == null || email.isEmpty()) {
            return ResponseEntity.badRequest().body("Email is required");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(new BCryptPasswordEncoder().encode(password));
        user.setEmail(email);

        User savedUser = userService.registerUser(user);
        return ResponseEntity.ok(savedUser);
    }

    @GetMapping
    public ResponseEntity<List<Task>> getUserTasks(@RequestParam Long userId) {
        List<Task> tasks = taskService.getTasksForUser(userId);
        if (tasks.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(tasks);
    }


    @GetMapping("/{taskId}/subtasks")
    public ResponseEntity<List<Task>> getSubtasks(@PathVariable Long taskId) {
        return ResponseEntity.ok(taskService.getSubtasks(taskId));
    }




    @PutMapping("/{taskId}")
    public ResponseEntity<Task> updateTask(@PathVariable Long taskId,
                                           @RequestParam String title,
                                           @RequestParam String description,
                                           @RequestParam(required = false) String dueDate,
                                           @RequestParam(required = false) String category,
                                           @RequestParam boolean completed) {
        LocalDateTime date = dueDate != null ? LocalDateTime.parse(dueDate, DateTimeFormatter.ISO_LOCAL_DATE_TIME) : null;

        Category categoryEntity = null;
        if (category != null) {
            categoryEntity = categoryService.getCategoryByName(category)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid category: " + category));
        }

        Task updated = taskService.updateTask(taskId, title, description, date, categoryEntity, completed);
        return ResponseEntity.ok(updated);
    }


    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.noContent().build();
    }

    // Yeni eklenen metodlar
    @GetMapping("/dashboard")
    public ResponseEntity<List<Task>> getDashboard(@AuthenticationPrincipal UserDetails userDetails) {
        Long userId = userService.findUserIdByUsername(userDetails.getUsername());
        List<Task> tasks = taskService.getTasksByUserId(userId);
        tasks.sort(Comparator.comparing(Task::getPriority).thenComparing(Task::getDueDate));
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/dashboard/priority")
    public ResponseEntity<List<Task>> getDashboardTasksByPriority(@RequestParam String priority) {
        return ResponseEntity.ok(taskService.getTasksByPriority(priority));
    }

    @GetMapping("/dashboard/status")
    public ResponseEntity<List<Task>> getDashboardTasksByStatus(@RequestParam String status) {
        return ResponseEntity.ok(taskService.getTasksByStatus(status));
    }



    @GetMapping("/dashboard/category")
    public ResponseEntity<List<Task>> getTasksByCategory(@RequestParam String categoryName) {
        Optional<Category> category = categoryService.getCategoryByName(categoryName);
        if (category.isEmpty()) {
            return ResponseEntity.badRequest().body(Collections.emptyList());
        }
        List<Task> tasks = taskService.getTasksByCategory(category.get());
        return ResponseEntity.ok(tasks);
    }







    @GetMapping("/dashboard/grouped")
    public ResponseEntity<Map<String, List<Task>>> getGroupedTasks(@AuthenticationPrincipal UserDetails userDetails) {
        Long userId = userService.findUserIdByUsername(userDetails.getUsername());
        List<Task> tasks = taskService.getTasksByUserId(userId);
        Map<String, List<Task>> groupedTasks = tasks.stream()
                .collect(Collectors.groupingBy(task -> task.getCategory().getName()));
        return ResponseEntity.ok(groupedTasks);
    }
}
