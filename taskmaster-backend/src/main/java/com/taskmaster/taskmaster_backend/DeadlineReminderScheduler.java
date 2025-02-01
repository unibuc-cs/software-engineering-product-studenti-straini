package com.taskmaster.taskmaster_backend;

import com.taskmaster.taskmaster_backend.model.Task;
import com.taskmaster.taskmaster_backend.repository.TaskRepository;
import com.taskmaster.taskmaster_backend.service.EmailService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.util.List;

@Component
public class DeadlineReminderScheduler {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private EmailService emailService;

//    @Scheduled(cron = "0 * * * * ?")  //pentru fiecare minut
    @Scheduled(cron = "0 0 9 * * ?")
    @Transactional
    public void sendDeadlineReminders() {
        LocalDate today = LocalDate.now();
        LocalDate upcomingDate = today.plusDays(1);

        List<Task> upcomingTasks = taskRepository.findAll().stream()
                .filter(task -> task.getDeadline() != null &&
                        !task.getDeadline().isBefore(today) &&
                        task.getDeadline().equals(upcomingDate))
                .toList();

        for (Task task : upcomingTasks) {
            String email = task.getUser().getEmail();
            String subject = "Reminder: Deadline pentru task-ul tău!";
            String body = "Task-ul tau, \"" + task.getTitle() + "\" are deadline pe " + task.getDeadline() + ".";

            emailService.sendEmail(email, subject, body);
        }
    }
}
