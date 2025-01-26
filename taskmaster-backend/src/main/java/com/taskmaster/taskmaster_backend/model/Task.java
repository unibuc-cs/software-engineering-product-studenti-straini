package com.taskmaster.taskmaster_backend.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference;


@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long task_id;

    private String title;
    private String description;
    private Boolean completed;


    @Enumerated(EnumType.STRING)
    private Priority priority;

    @ManyToOne(fetch = FetchType.LAZY) // many-to-one cu User
    @JoinColumn(name = "id")
    @JsonBackReference
    private User user;

    // Constructors
    public Task() {}

    public Task(String title, String description, boolean completed, Priority priority, User user) {
        this.title = title;
        this.description = description;
        this.completed = completed;
        this.priority = priority;
        this.user = user    ;
    }

    // Getter & Setter
    public Long getTaskId() { return task_id; }
    public void setTaskId(Long task_id) { this.task_id = task_id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }

    public Boolean getCompleted() { return completed; }


    public Priority getPriority() { return priority; }
    public void setPriority(Priority priority) { this.priority = priority; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}
