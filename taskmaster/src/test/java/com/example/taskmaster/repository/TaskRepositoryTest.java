package com.example.taskmaster.repository;

import com.example.taskmaster.model.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    @Test
    public void testFindByPriority() {
        // Test için örnek veri oluşturma
        Task task = new Task();
        task.setTitle("Test Task");
        task.setDescription("A high priority task");
        task.setPriority("High");
        taskRepository.save(task);

        // Metodu test etme
        List<Task> tasks = taskRepository.findByPriority("High");
        assertNotNull(tasks);               // Görevlerin null olmadığını kontrol et
        assertFalse(tasks.isEmpty());       // Görev listesinin boş olmadığını kontrol et
        assertEquals("High", tasks.get(0).getPriority()); // İlk görevin önceliğini kontrol et
    }
}
