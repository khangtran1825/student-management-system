package com.studentmanagement.init;

import com.studentmanagement.entity.User;
import com.studentmanagement.repository.UserRepository;
import io.quarkus.runtime.StartupEvent;
import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.jboss.logging.Logger;

@ApplicationScoped
public class DataInitializer {

    private static final Logger LOG = Logger.getLogger(DataInitializer.class);

    @Inject
    UserRepository userRepository;

    @Transactional
    public void onStart(@Observes StartupEvent ev) {
        LOG.info("Running DataInitializer...");
        try {
            ensureDefaultUsers();
        } catch (Exception e) {
            LOG.error("DataInitializer failed", e);
        }
    }

    private void ensureDefaultUsers() {
        createUserIfNotExists("admin", "admin123", "admin@student-management.local", "ADMIN");
        createUserIfNotExists("teacher", "teacher123", "teacher@student-management.local", "TEACHER");
        createUserIfNotExists("student", "student123", "student@student-management.local", "STUDENT");
    }

    private void createUserIfNotExists(String username, String password, String email, String role) {
        var existing = userRepository.find("username", username).firstResultOptional();
        if (existing.isPresent()) {
            LOG.info("User " + username + " already exists, skipping creation.");
            return;
        }

        User user = new User();
        user.username = username;
        user.password = BcryptUtil.bcryptHash(password);
        user.email = email;
        user.role = role;
        user.active = true;
        userRepository.persistAndFlush(user);
        LOG.info("Default user created: username=" + username + ", password=" + password + ", role=" + role);
    }
}
