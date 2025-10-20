package com.unitime.unitime_backend.service;

import com.unitime.unitime_backend.dto.appointment.AppointmentCreateDTO;
import com.unitime.unitime_backend.dto.appointment.AppointmentDeleteDTO;
import com.unitime.unitime_backend.dto.appointment.AppointmentResponseDTO;
import com.unitime.unitime_backend.entity.Appointment;
import com.unitime.unitime_backend.entity.User;
import com.unitime.unitime_backend.exception.ResourceNotFoundException;
import com.unitime.unitime_backend.mapper.AppointmentMapper;
import com.unitime.unitime_backend.repository.AppointmentRepository;
import com.unitime.unitime_backend.repository.GroupRepository;
import jakarta.transaction.Transactional;
import lombok.Data;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Data
@Transactional
public class AppointmentService {
    // TODO : implement crud if necessary
    private final AppointmentMapper appointmentMapper;
    private AppointmentRepository appointmentRepository;
    private final GroupRepository groupRepository;

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
    public void deleteAppointment(
            AppointmentDeleteDTO input
    ) {
        var appointment = appointmentRepository.findById(input.getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Appointment not found"
                ));

        appointmentRepository.delete(appointment);
    }

    public List<AppointmentResponseDTO> getAppointments(
            long groupId
    ) {

        List<User> users = groupRepository.findById(groupId)
                .orElseThrow( () -> new ResourceNotFoundException(" Group not found"))
                .getUsers();

        // Here we find the appointments created by other group members
        var appointments =  appointmentRepository.findAll()
                .stream()
                .filter(app -> users.contains(app.getCreatedBy()))
                .toList();

        return appointments
                .stream()
                .map(appointmentMapper::toResponseDTO)
                .toList();
    }
}
