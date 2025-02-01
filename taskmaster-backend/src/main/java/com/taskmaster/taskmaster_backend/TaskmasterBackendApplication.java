package com.taskmaster.taskmaster_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class TaskmasterBackendApplication {
	public static void main(String[] args) {
		SpringApplication.run(TaskmasterBackendApplication.class, args);
	}
}
