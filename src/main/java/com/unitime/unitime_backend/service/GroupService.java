package com.unitime.unitime_backend.service;

import com.unitime.unitime_backend.dto.group.GroupCreateDTO;
import com.unitime.unitime_backend.dto.group.GroupResponseDTO;
import com.unitime.unitime_backend.dto.group.GroupUpdateDTO;
import com.unitime.unitime_backend.entity.Group;
import com.unitime.unitime_backend.entity.User;
import com.unitime.unitime_backend.exception.ForbiddenException;
import com.unitime.unitime_backend.exception.ResourceNotFoundException;
import com.unitime.unitime_backend.mapper.GroupMapper;
import com.unitime.unitime_backend.repository.GroupRepository;
import com.unitime.unitime_backend.repository.PromotionRepository;
import com.unitime.unitime_backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.Data;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@Data
public class GroupService {

    private final GroupRepository groupRepository;
    private final GroupMapper groupMapper;
    private final PromotionRepository promotionRepository;
    private final UserRepository userRepository;

    public GroupResponseDTO createGroup(
            GroupCreateDTO request
    ) {
        var promotion = promotionRepository.findById(request.getPromotionId())
                .orElseThrow(()-> new ResourceNotFoundException("Promotion not found"));
        var user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Group group = Group.builder()
                .name(request.getName())
                .promotion(promotion)
                .accessCode(request.getAccessCode())
                .createdBy(user)
                .users(List.of(user))
                .build();

        groupRepository.save(group);
        user.setGroup(group);
        userRepository.save(user);
        return groupMapper.toResponseDTO(group);

    }

    public GroupResponseDTO updateGroup(
            GroupUpdateDTO request
    ) {
        String accessCode = request.getAccessCode();
        var group = groupRepository.findByAccessCode(accessCode)
                .orElseThrow(()->  new ResourceNotFoundException(
                        "Group not found"));

        if(request.getAccessCode().equals(accessCode)) {
            group.setAccessCode(accessCode);
        } else if ( request.getName().equals(group.getName())) {
            group.setName(request.getName());
        }

        groupRepository.save(group);
        return groupMapper.toResponseDTO(group);
    }

    public GroupResponseDTO getGroup(String accessCode) {
        var foundGroup = groupRepository.findByAccessCode(accessCode)
                .orElseThrow(()->  new ResourceNotFoundException(
                        "Group not found"));
        return groupMapper.toResponseDTO(foundGroup);
    }

    public void deleteGroup(
            String accessCode
    ) {
        var group = groupRepository.findByAccessCode(accessCode)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Group not found"
                ));
        var createdBy = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println("############"+createdBy);
        System.out.println("#############"+ group.getCreatedBy());
        if(createdBy.equals(group.getCreatedBy())) {

            List<User> groupUsers = group.getUsers();
            groupUsers.forEach(
                    user -> {
                        user.setGroup(null);
                    }
            );
            groupRepository.delete(group);

        } else {
            throw new ForbiddenException(
                    "FORBIDDEN"
            );
        }
    }
}
