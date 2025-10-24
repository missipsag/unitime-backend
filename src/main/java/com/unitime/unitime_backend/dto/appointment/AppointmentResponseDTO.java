package com.unitime.unitime_backend.dto.appointment;

import com.unitime.unitime_backend.dto.user.UserResponseDTO;
import com.unitime.unitime_backend.entity.AppointmentScope;
import com.unitime.unitime_backend.entity.AppointmentType;
import com.unitime.unitime_backend.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
public class AppointmentResponseDTO {
    private final long id;
    private final String title;
    private final String location;
    private final Timestamp startTime;
    private final Timestamp endTime;
    private final AppointmentType appointmentType;
    private final AppointmentScope appointmentScope;
    private final String recurrenceRule;
    private final UserResponseDTO createdBy;
}
