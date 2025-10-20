package com.unitime.unitime_backend.repository;

import com.unitime.unitime_backend.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group, Long> {
    Optional<Group> findByAccessCode(String accessCode);
}
