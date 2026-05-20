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
            ensureAdminUser();
        } catch (Exception e) {
            LOG.error("DataInitializer failed", e);
        }
    }

    private void ensureAdminUser() {
        var existing = userRepository.find("username", "admin").firstResultOptional();
        if (existing.isPresent()) {
            LOG.info("Admin user already exists, skipping creation.");
            return;
        }

        User admin = new User();
        admin.username = "admin";
        admin.password = BcryptUtil.bcryptHash("admin123");
        admin.email = "admin@student-management.local";
        admin.role = "ADMIN";
        admin.active = true;
        userRepository.persistAndFlush(admin);
        LOG.info("Default admin user created: username=admin, password=admin123");
    }
}
