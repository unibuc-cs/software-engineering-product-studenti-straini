package com.taskmaster.taskmaster_backend.repository;

import com.taskmaster.taskmaster_backend.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByUser_Id(Long id);
    List<Task> findByDeadline(LocalDate deadline);
}

