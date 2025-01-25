package com.taskmaster.taskmaster_backend.controller;

import com.taskmaster.taskmaster_backend.model.Task;
import com.taskmaster.taskmaster_backend.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tasks")  // 🌟 Bütün endpointler "/api/tasks" üzerinden çalışacak
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    // ✅ Yeni Görev Ekle
    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        Task savedTask = taskService.save(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTask);
    }

    // ✅ Tüm Görevleri Getir
    @GetMapping
    public List<Task> getAllTasks() {
        return taskService.findAll();
    }

    // ✅ Belirli ID'ye Göre Görevi Getir
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        Optional<Task> task = taskService.findById(id);
        return task.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // ✅ Görevi Güncelle (Tam Güncelleme - PUT)
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task updatedTask) {
        return taskService.findById(id)
                .map(task -> {
                    task.setTitle(updatedTask.getTitle());
                    task.setDescription(updatedTask.getDescription());
                    task.setCompleted(updatedTask.isCompleted());  // HATA DÜZELTİLDİ ✅
                    task.setUserId(updatedTask.getUserId());
                    return ResponseEntity.ok(taskService.save(task));
                }).orElse(ResponseEntity.notFound().build());
    }

    // ✅ Görevi Kısmi Güncelleme (PATCH)
    @PatchMapping("/{id}")
    public ResponseEntity<Task> partiallyUpdateTask(@PathVariable Long id, @RequestBody Task updatedTask) {
        return taskService.findById(id)
                .map(task -> {
                    if (updatedTask.getTitle() != null) task.setTitle(updatedTask.getTitle());
                    if (updatedTask.getDescription() != null) task.setDescription(updatedTask.getDescription());
                    if (updatedTask.getUserId() != null) task.setUserId(updatedTask.getUserId());

                    // ✅ Boolean için null kontrolü düzeltildi:
                    if (updatedTask.getCompleted() != null) task.setCompleted(Boolean.TRUE.equals(updatedTask.getCompleted()));

                    return ResponseEntity.ok(taskService.save(task));
                }).orElse(ResponseEntity.notFound().build());
    }



    // ✅ Görevi Sil (DELETE)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        if (taskService.findById(id).isPresent()) {
            taskService.delete(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
