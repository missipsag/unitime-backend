package com.unitime.unitime_backend.service;

import com.unitime.unitime_backend.dto.promotion.PromotionCreateDTO;
import com.unitime.unitime_backend.dto.promotion.PromotionResponseDTO;
import com.unitime.unitime_backend.entity.Group;
import com.unitime.unitime_backend.entity.Promotion;
import com.unitime.unitime_backend.entity.User;
import com.unitime.unitime_backend.exception.ForbiddenException;
import com.unitime.unitime_backend.exception.ResourceNotFoundException;
import com.unitime.unitime_backend.mapper.PromotionMapper;
import com.unitime.unitime_backend.repository.GroupRepository;
import com.unitime.unitime_backend.repository.PromotionRepository;
import com.unitime.unitime_backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.Data;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Data
@Transactional
public class PromotionService {

    private final PromotionMapper promotionMapper;
    private final PromotionRepository promotionRepository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    public PromotionResponseDTO createPromotion(
            PromotionCreateDTO input
    ) {
        var user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var promotion = Promotion.builder()
                .name(input.getName())
                .field(input.getField())
                .promotionLevel(input.getPromotionLevel())
                .accessCode(input.getAccessCode())
                .groups(List.of())
                .createdBy(user)
                .build();

        user.setPromotion(promotion);
        promotionRepository.save(promotion);
        userRepository.save(user);

        return  promotionMapper.toResponseDTO(promotion);
    }

    public PromotionResponseDTO getPromotion(
            String accessCode
    ) {
        var promotion = promotionRepository.findByAccessCode(accessCode)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Promotion not found"
                ));

        return promotionMapper.toResponseDTO(promotion);
    }

    public void deletePromotion(
            String accessCode
    ) {
        var user =(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var promotion = promotionRepository.findByAccessCode(accessCode)
                .orElseThrow(() -> new ResourceNotFoundException("Promotion not found"));

        if(user.equals(promotion.getCreatedBy())) {

            List<Group> groups = promotion.getGroups();
            groups.forEach(
                    group -> {
                        group.getUsers().forEach(
                                usr -> {
                                    usr.setGroup(null);
                                    usr.setPromotion(null);
                                }
                        );
                        groupRepository.delete(group);
                    }
            );
            promotionRepository.delete(promotion);
        }else {
            throw new ForbiddenException("Forbidden");
        }
    }
}
