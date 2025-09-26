package com.unitime.unitime_backend.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UnauthorizedException extends BaseException{

    public UnauthorizedException(String message) {
        super(message, "UNAUTHORIZED", 401);
    }

    public UnauthorizedException() {
        super("Authentication required", "UNAUTHORIZED", 401);
    }
}
