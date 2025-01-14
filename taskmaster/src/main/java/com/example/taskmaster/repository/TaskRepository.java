package com.example.taskmaster.repository;

import com.example.taskmaster.model.Category;
import com.example.taskmaster.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByUserId(Long userId);
    List<Task> findByPriorityOrderByDueDateAsc(String priority);
    List<Task> findByDueDateBefore(LocalDateTime date);
    List<Task> findByDueDateBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
    List<Task> findByStatus(String status);
    List<Task> findByPriority(String priority);

    @Query("SELECT t FROM Task t WHERE t.category = :category")
    List<Task> findByCategory(@Param("category") Category category);

    int countByUserIdAndCompleted(Long userId, boolean completed);
}
