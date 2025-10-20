package com.unitime.unitime_backend.dto.promotion;

import com.unitime.unitime_backend.entity.PromotionLevel;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PromotionResponseDTO {
    private long id;
    @NotBlank(message = "Promotion name is required")
    private String name;
    @NotBlank(message = "promotion field is required")
    private String field;
    @NotBlank(message = "promotion level is required")
    private PromotionLevel promotionLevel;
}
