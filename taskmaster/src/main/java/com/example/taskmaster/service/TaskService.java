package com.example.taskmaster.service;

import com.example.taskmaster.model.Category;
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
    public Task createTask(User user, String title, String description, LocalDateTime dueDate, Category category) {
        Task task = new Task();
        task.setUser(user);
        task.setTitle(title);
        task.setDescription(description);
        task.setDueDate(dueDate);
        task.setCategory(category);
        return taskRepository.save(task);
    }




    public List<Task> getTasksByStatus(String status) {
        return taskRepository.findByStatus(status);
    }

    public List<Task> getSubtasks(Long taskId) {
        Task parentTask = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        return parentTask.getSubtasks();
    }


    public List<Task> getTasksByUserId(Long userId) {
        return taskRepository.findByUserId(userId);
    }



    public List<Task> getTasksByPriority(String priority) {
        return taskRepository.findByPriorityOrderByDueDateAsc(priority);
    }

    public List<Task> getTasksByCategory(Category category) {
        return taskRepository.findByCategory(category);
    }



    public List<Task> getTasksForUser(Long userId) {
        return taskRepository.findByUserId(userId);
    }


    public Task updateTask(Long taskId, String title, String description, LocalDateTime dueDate, Category category, boolean completed) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new RuntimeException("Task not found"));
        task.setTitle(title);
        task.setDescription(description);
        task.setDueDate(dueDate);
        task.setCategory(category);
        task.setCompleted(completed);
        return taskRepository.save(task);
    }




    public void deleteTask(Long taskId) {
        taskRepository.deleteById(taskId);
    }

    public Task save(Task task) {
        return taskRepository.save(task);
    }
}
