package com.unitime.unitime_backend.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDTO {
    private long id;
    private String email;
    private String  firstName;
    private String lastName;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
