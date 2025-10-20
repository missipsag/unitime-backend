package com.unitime.unitime_backend.mapper;


import com.unitime.unitime_backend.dto.group.GroupCreateDTO;
import com.unitime.unitime_backend.dto.group.GroupResponseDTO;
import com.unitime.unitime_backend.entity.Group;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GroupMapper {

    GroupResponseDTO toResponseDTO(Group group);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "users", ignore = true)
    @Mapping(target = "promotion", ignore = true)
    Group toEntity(GroupCreateDTO groupCreateDTO);

}
