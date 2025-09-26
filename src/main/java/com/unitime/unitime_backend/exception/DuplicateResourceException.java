package com.unitime.unitime_backend.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DuplicateResourceException extends BaseException{

    public DuplicateResourceException(String message) {
        super(message, "DUPLICATE_RESOURCE", 409);
    }

    public DuplicateResourceException(String resourceType, String field, String value) {
        super(String.format("%s with %s '%s' already exists", resourceType, field, value), "DUPLICATE_RESOURCE", 409);
    }
}
