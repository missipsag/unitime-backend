package com.unitime.unitime_backend.mapper;

import com.unitime.unitime_backend.dto.appointment.AppointmentCreateDTO;
import com.unitime.unitime_backend.dto.appointment.AppointmentResponseDTO;
import com.unitime.unitime_backend.entity.Appointment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {
    AppointmentResponseDTO toResponseDTO(Appointment appointment);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    Appointment toEntity(AppointmentCreateDTO appointmentCreateDTO);


}
