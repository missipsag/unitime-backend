package com.unitime.unitime_backend.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ValidationException extends BaseException{

    public ValidationException(String message ) {
        super(message, "VALIDATION_ERROR",400);
    }

    public ValidationException(String field, String reason) {
        super(String.format("Validation failed for field : %s : %s", field, reason), "VALIDATION_ERROR", 400);
    }
}
