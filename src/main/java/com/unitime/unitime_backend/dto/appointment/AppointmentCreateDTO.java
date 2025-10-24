package com.unitime.unitime_backend.dto.appointment;

import com.unitime.unitime_backend.entity.AppointmentScope;
import com.unitime.unitime_backend.entity.AppointmentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
public class AppointmentCreateDTO {
    private final String title;
    private final String location;
    private final Timestamp startTime;
    private final Timestamp endTime;
    private final AppointmentType appointmentType;
    private final AppointmentScope appointmentScope;
    private final String recurrenceRule;
}
