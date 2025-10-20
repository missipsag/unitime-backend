package com.unitime.unitime_backend.dto.promotion;

import com.unitime.unitime_backend.entity.PromotionLevel;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PromotionCreateDTO {
    @NotBlank(message = "Promotion name is required")
    private String name;
    @NotBlank(message = "Promotion access code in required")
    private String accessCode;
    @NotBlank(message = "Promotion level is required")
    private PromotionLevel promotionLevel;
    @NotBlank(message = "Promotion field is required")
    private String field;
}
