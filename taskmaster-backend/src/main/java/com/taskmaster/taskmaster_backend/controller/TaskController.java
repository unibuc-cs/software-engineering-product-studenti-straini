package com.taskmaster.taskmaster_backend.controller;

import com.taskmaster.taskmaster_backend.model.Task;
import com.taskmaster.taskmaster_backend.model.Priority;
import com.taskmaster.taskmaster_backend.service.TaskService;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tasks")  // 🌟 Toate endpoint-urile vor funcționa prin „/api/tasks”.
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    // ✅ Adauga un nou task
    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        Task savedTask = taskService.save(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTask);
    }

    // ✅ Obtine toate task urile
    @GetMapping
    @Transactional
    public List<Task> getAllTasks() {
        return taskService.findAll();
    }

    // ✅ Obtine task-ul dupa un anumit ID
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        return taskService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    //✅ Obtine toate task-urile dupa un anumit ID al unui utilizator
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Task>> getTasksByUserId(@PathVariable Long userId) {
        List<Task> tasks = taskService.getTasksByUserId(userId);
        return ResponseEntity.ok(tasks);
    }


    // ✅ Actualizează task-ul (Actualizare completă - PUT).
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task updatedTask) {
        try {
            Task updated = taskService.update(id, updatedTask);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // ✅ Actualizează parțial task-ul (PATCH).
    @PatchMapping("/{id}")
    public ResponseEntity<Task> partiallyUpdateTask(@PathVariable Long id, @RequestBody Task updatedTask) {
        return taskService.findById(id)
                .map(task -> {
                    // Actualizează câmpurile doar dacă ele nu sunt null
                    if (updatedTask.getTitle() != null) {
                        task.setTitle(updatedTask.getTitle());
                    }
                    if (updatedTask.getDescription() != null) {
                        task.setDescription(updatedTask.getDescription());
                    }
                    if (updatedTask.getPriority() != null) {
                        task.setPriority(updatedTask.getPriority());
                    }
                    if (updatedTask.getCompleted() != null) {
                        task.setCompleted(updatedTask.getCompleted());
                    }

                    // Verifică și asociază un utilizator dacă este specificat
                    if (updatedTask.getUser() != null && updatedTask.getUser().getId() != null) {
                        task.setUser(updatedTask.getUser());
                    }

                    Task savedTask = taskService.save(task);
                    return ResponseEntity.ok(savedTask);
                })
                .orElse(ResponseEntity.notFound().build());
    }




    // ✅ Șterge task-ul (DELETE).
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        if (taskService.findById(id).isPresent()) {
            taskService.delete(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
