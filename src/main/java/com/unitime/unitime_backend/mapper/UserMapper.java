package com.unitime.unitime_backend.mapper;

import com.unitime.unitime_backend.dto.user.UserRegisterDTO;
import com.unitime.unitime_backend.dto.user.UserResponseDTO;
import com.unitime.unitime_backend.dto.user.UserUpdateDTO;
import com.unitime.unitime_backend.entity.User;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    // to transform a user into a response dto
    UserResponseDTO toResponseDTO(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "group", ignore = true)
    @Mapping(target = "promotion", ignore = true)
    @Mapping(target = "role", ignore = true)
    User toEntity(UserRegisterDTO userCreateDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "group", ignore = true)
    @Mapping(target = "promotion", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    void updateUserFromDTO(UserUpdateDTO userUpdateDTO, @MappingTarget User user);

    List<UserResponseDTO> toResponseDTOList(List<User> users);
}
