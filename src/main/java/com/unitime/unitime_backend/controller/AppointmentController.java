package com.unitime.unitime_backend.controller;

import com.unitime.unitime_backend.dto.appointment.AppointmentCreateDTO;
import com.unitime.unitime_backend.dto.appointment.AppointmentResponseDTO;
import com.unitime.unitime_backend.service.AppointmentService;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/appointments")
@Data
public class AppointmentController {

    private final AppointmentService appointmentService;
    @PostMapping("/create")
    public ResponseEntity<AppointmentResponseDTO> createAppointment(@RequestBody AppointmentCreateDTO request) {
        var newAppointment = appointmentService.createAppointment(request);

        return ResponseEntity.ok(newAppointment);
    }

}
