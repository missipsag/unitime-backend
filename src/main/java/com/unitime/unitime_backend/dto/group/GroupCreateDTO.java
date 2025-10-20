package com.unitime.unitime_backend.dto.group;

import com.unitime.unitime_backend.entity.Promotion;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroupCreateDTO {
    @NotBlank(message = "Group name is required")
    private String name;

    @NotBlank(message = "Group access code in required")
    private String accessCode;

    private long promotionId;
}
