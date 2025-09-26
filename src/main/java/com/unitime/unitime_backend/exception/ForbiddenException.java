package com.unitime.unitime_backend.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ForbiddenException extends BaseException{

    public ForbiddenException(String message) {
        super(message, "FORBIDDEN", 403);
    }

    public ForbiddenException() {
        super("Access denied", "FORBIDDEN", 403);
    }
}
