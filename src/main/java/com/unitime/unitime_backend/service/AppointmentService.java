package com.unitime.unitime_backend.service;

import com.unitime.unitime_backend.dto.appointment.AppointmentCreateDTO;
import com.unitime.unitime_backend.dto.appointment.AppointmentDeleteDTO;
import com.unitime.unitime_backend.dto.appointment.AppointmentResponseDTO;
import com.unitime.unitime_backend.entity.Appointment;
import com.unitime.unitime_backend.entity.AppointmentScope;
import com.unitime.unitime_backend.entity.User;
import com.unitime.unitime_backend.exception.ResourceNotFoundException;
import com.unitime.unitime_backend.mapper.AppointmentMapper;
import com.unitime.unitime_backend.mapper.UserMapper;
import com.unitime.unitime_backend.repository.AppointmentRepository;
import com.unitime.unitime_backend.repository.GroupRepository;
import com.unitime.unitime_backend.repository.PromotionRepository;
import jakarta.transaction.Transactional;
import lombok.Data;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Data
@Transactional
public class AppointmentService {
    private final AppointmentMapper appointmentMapper;
    private final AppointmentRepository appointmentRepository;
    private final GroupRepository groupRepository;
    private final PromotionRepository promotionRepository;
    private final UserMapper userMapper;

    public AppointmentResponseDTO createAppointment(AppointmentCreateDTO input) {
        User createBy =  (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Appointment appointment =  Appointment.builder()
                .title(input.getTitle())
                .location(input.getLocation())
                .startTime(input.getStartTime())
                .endTime(input.getEndTime())
                .appointmentType(input.getAppointmentType())
                .recurrenceRule(input.getRecurrenceRule())
                .appointmentScope(input.getAppointmentScope())
                .createdBy(createBy)
                .build();
        appointmentRepository.save(appointment);

        return AppointmentResponseDTO.builder()
                .id(appointment.getId())
                .title(appointment.getTitle())
                .location(appointment.getLocation())
                .startTime(appointment.getStartTime())
                .endTime(appointment.getEndTime())
                .appointmentType(appointment.getAppointmentType())
                .recurrenceRule(appointment.getRecurrenceRule())
                .appointmentScope(appointment.getAppointmentScope())
                .createdBy(userMapper.toResponseDTO(createBy))
                .build();
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

    public List<AppointmentResponseDTO> getAppointments() {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User groupCreatedBy = groupRepository.findById(user.getGroup().getId())
                .orElseThrow( () -> new ResourceNotFoundException("Group not found"))
                .getCreatedBy();

        User promotionCreatedBy = promotionRepository.findById(user.getPromotion().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Promotion not found"))
                .getCreatedBy();

        // Here we find the appointments created by other group members and promotion members
        var appointments =  appointmentRepository.findAll()
                .stream()
                .filter(app -> {
                    return (app.getCreatedBy() == groupCreatedBy && app.getAppointmentScope() == AppointmentScope.GROUP) ||
                            (app.getCreatedBy() == promotionCreatedBy && app.getAppointmentScope() == AppointmentScope.PROMOTION);
                })
                .toList();

        return appointments
                .stream()
                .map(appointmentMapper::toResponseDTO)
                .toList();
    }
}
