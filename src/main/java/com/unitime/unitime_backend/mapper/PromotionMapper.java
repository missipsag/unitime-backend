package com.unitime.unitime_backend.mapper;
import com.unitime.unitime_backend.dto.promotion.PromotionCreateDTO;
import com.unitime.unitime_backend.dto.promotion.PromotionResponseDTO;
import com.unitime.unitime_backend.entity.Promotion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PromotionMapper {

    PromotionResponseDTO toResponseDTO(Promotion promotion);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target="createdBy", ignore = true)
    @Mapping(target = "groups", ignore = true)
    Promotion toEntity(PromotionCreateDTO promotionCreateDTO);
}
