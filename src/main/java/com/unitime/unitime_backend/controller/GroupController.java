package com.unitime.unitime_backend.controller;

import com.unitime.unitime_backend.dto.group.*;
import com.unitime.unitime_backend.service.GroupService;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/groups")
@Data
public class GroupController {
    private final GroupService groupService;

    @PostMapping("/create")
    public ResponseEntity<GroupResponseDTO> createGroup(
            @RequestBody GroupCreateDTO request
    ) {
        var newGroup =  groupService.createGroup(request);
        return ResponseEntity.ok(newGroup);
    }

    @PostMapping("/update")
    public ResponseEntity<GroupResponseDTO> updateGroup(
            @RequestBody GroupUpdateDTO request
            ) {
        return ResponseEntity.ok(groupService.updateGroup(request)) ;
    }

    @GetMapping("/get")
    public ResponseEntity<GroupResponseDTO> getGroup(
            @RequestBody GroupGetDTO accessCode
    ) {
        return ResponseEntity.ok(groupService.getGroup(accessCode.getAccessCode()));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteGroup(
            @RequestBody GroupDeleteDTO request
            ) {
        groupService.deleteGroup(request.getAccessCode());
        return ResponseEntity.ok().build();
    }


}
