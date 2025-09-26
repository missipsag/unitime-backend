package com.unitime.unitime_backend.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ResourceNotFoundException extends  BaseException{

    public ResourceNotFoundException(String message) {
        super(message, "RESOURCE_NOT_FOUND", 404);
    }

    public ResourceNotFoundException(String resourceType, String identifier) {
        super(String.format("%s with identifier : '%s' not found", resourceType, identifier), "RESOURCE_NOT_FOUND",404);
    }

}
