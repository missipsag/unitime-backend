package com.unitime.unitime_backend.controller;

import com.unitime.unitime_backend.dto.appointment.AppointmentCreateDTO;
import com.unitime.unitime_backend.dto.appointment.AppointmentDeleteDTO;
import com.unitime.unitime_backend.dto.appointment.AppointmentResponseDTO;
import com.unitime.unitime_backend.mapper.AppointmentMapper;
import com.unitime.unitime_backend.repository.GroupRepository;
import com.unitime.unitime_backend.service.AppointmentService;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@Data
public class AppointmentController {
    private final GroupRepository groupRepository;
    private final AppointmentService appointmentService;
    private final AppointmentMapper appointmentMapper;

    @PostMapping("/create")
    public ResponseEntity<AppointmentResponseDTO> createAppointment(@RequestBody AppointmentCreateDTO request) {
        var newAppointment = appointmentService.createAppointment(request);

        return ResponseEntity.ok(newAppointment);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteAppointment(
            @RequestBody AppointmentDeleteDTO request
            ) {
        appointmentService.deleteAppointment(request);
        return ResponseEntity.ok().build();
    }

    // todo : this code only return group appointments. It should return both group and section level appointments

    @GetMapping("/get")
    public ResponseEntity<List<AppointmentResponseDTO>> getAppointments() {
        List<AppointmentResponseDTO> appointments = appointmentService.getAppointments();
        return ResponseEntity.ok(appointments);
    }

}
