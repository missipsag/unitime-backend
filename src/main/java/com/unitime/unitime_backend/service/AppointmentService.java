package com.unitime.unitime_backend.service;

import com.unitime.unitime_backend.dto.appointment.AppointmentCreateDTO;
import com.unitime.unitime_backend.dto.appointment.AppointmentResponseDTO;
import com.unitime.unitime_backend.entity.Appointment;
import com.unitime.unitime_backend.entity.User;
import com.unitime.unitime_backend.mapper.AppointmentMapper;
import com.unitime.unitime_backend.repository.AppointmentRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AppointmentService {
    // TODO : implement crud if necessary
    private final AppointmentMapper appointmentMapper;
    private AppointmentRepository appointmentRepository;

    public AppointmentResponseDTO createAppointment(AppointmentCreateDTO input) {
        Appointment appointment =  Appointment.builder()
                .title(input.getTitle())
                .location(input.getLocation())
                .startTime(input.getStartTime())
                .endTime(input.getEndTime())
                .appointmentType(input.getAppointmentType())
                .recurrenceRule(input.getRecurrenceRule())
                .build();
        var currUser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        appointment.setCreatedBy(currUser);
        appointmentRepository.save(appointment);
        return appointmentMapper.toResponseDTO(appointment);
    }

}
