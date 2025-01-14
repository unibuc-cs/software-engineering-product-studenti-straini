package com.example.taskmaster;

import static org.mockito.Mockito.*;

import com.example.taskmaster.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import jakarta.mail.internet.MimeMessage;

class NotificationServiceTest {

    @Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    private NotificationService notificationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSendDeadlineNotifications() throws Exception {
        // Mock MimeMessage
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

        // Simüle edilen davranış
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        mimeMessageHelper.setTo("test@example.com");
        mimeMessageHelper.setSubject("Test Subject");
        mimeMessageHelper.setText("Test Body");

        // Test methodunu çağırma
        notificationService.sendEmail("test@example.com", "Test Subject", "Test Body");

        // Doğrulama
        verify(javaMailSender, times(1)).send(mimeMessage);
    }
}
