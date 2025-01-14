package com.example.taskmaster.service;

import com.example.taskmaster.model.Task;
import com.example.taskmaster.model.User;
import com.example.taskmaster.repository.TaskRepository;
import com.example.taskmaster.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {

    private final TaskRepository taskRepository;
    private UserRepository userRepository = null;
    private final JavaMailSender mailSender;

    public NotificationService(TaskRepository taskRepository, JavaMailSender mailSender) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.mailSender = mailSender;
    }

    public void sendEmail(String to, String subject, String content) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }

    public void testEmail() {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo("test@example.com");
            message.setSubject("Test Mail");
            message.setText("This is a test mail.");
            mailSender.send(message);
            System.out.println("Email sent successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Scheduled(cron = "0 0 * * * *") // Her saat başı çalışır
    public void sendDeadlineNotifications() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime upcomingDeadline = now.plusDays(1); // 1 gün içinde yaklaşan görevler

        List<Task> tasks = taskRepository.findByDueDateBetween(now, upcomingDeadline);

        for (Task task : tasks) {
            if (task.getUser() != null && task.getUser().getEmail() != null) {
                String emailContent = String.format(
                        "Merhaba %s,\n\n'%s' başlıklı görevinizin teslim tarihi %s. Lütfen zamanında tamamlamayı unutmayın.",
                        task.getUser().getUsername(),
                        task.getTitle(),
                        task.getDueDate()
                );
                sendEmail(task.getUser().getEmail(), "Görev Hatırlatma", emailContent);
            }
        }
    }


    @Scheduled(fixedRate = 60000) // Her dakika çalışır
    public void checkTasksAndSendReminders() {
        List<Task> tasks = taskRepository.findAll();
        tasks.stream()
                .filter(task -> task.getDueDate() != null && task.getUser() != null)
                .filter(task -> task.getDueDate().isBefore(LocalDateTime.now().plusDays(1)))
                .forEach(this::sendNotification);
    }

    public void sendNotification(Long userId, String message) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (user.isNotificationsEnabled()) {
            System.out.println("Notification sent to user: " + user.getUsername() + " - " + message);
        }
    }

    public void sendNotification(Task task) {
        User user = task.getUser();
        if (user != null && user.isNotificationsEnabled()) {
            String message = "Reminder: Your task '" + task.getTitle() + "' is due on " + task.getDueDate();
            sendNotification(user.getId(), message);
        }
    }

    public void updateUserSettings(Long userId, User updatedUser) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setNotificationsEnabled(updatedUser.isNotificationsEnabled());
        userRepository.save(user);
    }

    public void sendReminder(User user, Task task) {
        if (user.isNotificationsEnabled()) {
            String subject = "Task Reminder: " + task.getTitle();
            String message = "Dear " + user.getUsername() + ",\n\n" +
                    "This is a reminder for your task:\n" +
                    "Title: " + task.getTitle() + "\n" +
                    "Due Date: " + task.getDueDate() + "\n\n" +
                    "Best regards,\nTaskMaster Team";
            sendEmail(user.getEmail(), subject, message);
            System.out.println("Reminder sent to " + user.getEmail());
        }
    }

}
