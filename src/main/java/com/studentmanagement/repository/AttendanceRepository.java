package com.studentmanagement.repository;

import com.studentmanagement.entity.Attendance;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AttendanceRepository implements PanacheRepository<Attendance> {
}
