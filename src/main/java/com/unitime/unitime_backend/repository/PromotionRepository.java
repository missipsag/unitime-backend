package com.unitime.unitime_backend.repository;

import com.unitime.unitime_backend.entity.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PromotionRepository extends JpaRepository<Promotion, Long> {
    Optional<Promotion> findByAccessCode(String accessCode);
}
