package com.example.taskmaster.service;

import com.example.taskmaster.model.Task;
import com.example.taskmaster.model.User;
import com.example.taskmaster.repository.TaskRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
@Service
public class TaskService {
    private final TaskRepository taskRepository;
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }
    public Task createTask(User user, String title, String description, LocalDateTime dueDate) {
        Task task = new Task();
        task.setUser(user);
        task.setTitle(title);
        task.setDescription(description);
        task.setDueDate(dueDate);
        return taskRepository.save(task);
    }
    public List<Task> getTasksForUser(Long userId) {
        return taskRepository.findByUserId(userId);
    }
    public Task updateTask(Long taskId, String title, String description, LocalDateTime dueDate, boolean completed) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new RuntimeException("Task not found"));
        task.setTitle(title);
        task.setDescription(description);
        task.setDueDate(dueDate);
        task.setCompleted(completed);
        return taskRepository.save(task);
    }
    public void deleteTask(Long taskId) {
        taskRepository.deleteById(taskId);
    }
}
