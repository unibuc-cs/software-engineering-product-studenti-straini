package com.example.taskmaster;

import com.example.taskmaster.model.User;
import com.example.taskmaster.repository.UserRepository;
import com.example.taskmaster.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Test
    public void testUpdateUserProfile() {
        // Kullanıcıyı kaydet
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("password");
        user = userRepository.save(user);

        // Kullanıcı e-posta ve bildirim ayarlarını güncelle
        user.setEmail("updatedemail@example.com");
        user.setEmailNotificationsEnabled(false);
        User updatedUser = userService.save(user);

        // Güncellenen bilgileri kontrol et
        assertNotNull(updatedUser);
        assertEquals("updatedemail@example.com", updatedUser.getEmail());
        assertFalse(updatedUser.isEmailNotificationsEnabled());
    }
}
