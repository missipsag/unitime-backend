package com.unitime.unitime_backend.dto.error;

import lombok.Data;

@Data
public class ValidationError {
    private String field;
    private String message;
    private Object rejectedValue;

    public ValidationError(String field, String message, Object rejectedValue) {
        this.field = field;
        this.message = message;
        this.rejectedValue = rejectedValue;
    }



}



