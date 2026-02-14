package com.ziery.DeltaForceLoadouts.security.bootstrap;


import com.ziery.DeltaForceLoadouts.security.entity.User;
import com.ziery.DeltaForceLoadouts.security.entity.UserRoles;
import com.ziery.DeltaForceLoadouts.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminUserInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Value("${app.bootstrap-admin:false}")
    private boolean enabled;

    @Value("${admin.username:}")
    private String adminUsername;
    @Value("${admin.password:}")
    private String adminPassword;
    @Value("${admin.email:}")
    private String adminEmail;

    public void run(String... args) {
        if (!enabled) return;

        if (adminUsername == null || adminUsername.isBlank()) {
            throw new IllegalStateException("Username admin vazio ou nulo!");
        }
        if (adminEmail == null || adminEmail.isBlank()) {
            throw new IllegalStateException("Email admin vazio ou nulo!");
        }
        if (adminPassword == null || adminPassword.isBlank()) {
            throw new IllegalStateException("Senha admin password vazio ou nulo!");
        }

        if (userRepository.findByUsername(adminUsername).isPresent()) {
            return;
        }



        User admin = new User();
        admin.setUsername(adminUsername);
        admin.setEmail(adminEmail);
        admin.setPassword(passwordEncoder.encode(adminPassword));
        admin.setRole(UserRoles.ADMIN);

        userRepository.save(admin);

        System.out.println("ADMIN criado com sucesso!");
    }
}