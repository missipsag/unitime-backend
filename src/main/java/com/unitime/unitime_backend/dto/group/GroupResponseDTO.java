package com.unitime.unitime_backend.dto.group;

import com.unitime.unitime_backend.dto.promotion.PromotionResponseDTO;
import com.unitime.unitime_backend.dto.user.UserResponseDTO;

import lombok.Builder;
import lombok.Data;

import java.util.List;


@Data
@Builder
public class GroupResponseDTO {
    private long id;
    private String name;
    private PromotionResponseDTO promotion;
    private List<UserResponseDTO> users;
    private String accessCode;
}
