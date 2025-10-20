package com.unitime.unitime_backend.controller;

import com.unitime.unitime_backend.dto.promotion.PromotionCreateDTO;
import com.unitime.unitime_backend.dto.promotion.PromotionGetDTO;
import com.unitime.unitime_backend.dto.promotion.PromotionResponseDTO;
import com.unitime.unitime_backend.repository.PromotionRepository;
import com.unitime.unitime_backend.service.PromotionService;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Data
@Builder
@RequestMapping("/api/promotions")
public class PromotionController {

    private PromotionService promotionService;

    @PostMapping("/create")
    public ResponseEntity<PromotionResponseDTO> createPromotion(
            @RequestBody PromotionCreateDTO request
            ) {
        var newGroup = promotionService.createPromotion(request);
        return ResponseEntity.ok(newGroup);
    }

    @GetMapping("/get")
    public ResponseEntity<PromotionResponseDTO> getPromotion(
            @RequestBody PromotionGetDTO accessCode
    ) {
        var promotion= promotionService.getPromotion(accessCode.getAccessCode());
        return ResponseEntity.ok(promotion);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deletePromotion(
            @RequestBody PromotionGetDTO accessCode
    ) {
        promotionService.deletePromotion(accessCode.getAccessCode());
        return ResponseEntity.ok().build();
    }

}
