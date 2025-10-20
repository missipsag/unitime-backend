package com.unitime.unitime_backend.dto.group;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GroupUpdateDTO {
    private String name;
    private String accessCode;
}
