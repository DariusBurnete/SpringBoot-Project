package com.example.demo;

import com.example.demo.model.Task;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.TaskRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class DataLoader {

    @Bean
    CommandLineRunner loadData(TaskRepository taskRepository, UserRepository userRepository) {
        return args -> {

            // Define the owner email and name
            String email = "john.doe@example.com";
            String name = "John Doe";

            // Check if the owner with this email already exists
            if (!userRepository.existsByEmail(email)) {
                // If the owner does not exist, create and save the owner
                User user = new User();
                user.setName(name);
                user.setEmail(email);
                userRepository.save(user);

                // Create and save a sample task for the owner
                Task task = new Task();
                task.setTitle("Sample Task");
                task.setDescription("This is a test task");
                task.setEstimatedHours(10);
                task.setCompletedHours(5);
                task.setRemainingEffort(5);
                task.setUser(user);

                taskRepository.save(task);
            } else {
                // Optionally log or print a message if the owner already exists
                System.out.println("User with email " + email + " already exists. Skipping creation.");
            }
        };
    }
}
