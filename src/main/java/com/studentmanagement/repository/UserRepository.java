// File: src/main/java/com/studentmanagement/repository/UserRepository.java
package com.studentmanagement.repository;

import com.studentmanagement.entity.User;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserRepository implements PanacheRepository<User> {
}