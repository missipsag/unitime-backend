package com.unitime.unitime_backend.repository;

import com.unitime.unitime_backend.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
}
