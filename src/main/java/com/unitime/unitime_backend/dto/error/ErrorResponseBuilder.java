package com.unitime.unitime_backend.dto.error;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class ErrorResponseBuilder {
    private ErrorResponse errorResponse;

    public  ErrorResponseBuilder () {
        this.errorResponse = new ErrorResponse();
    }

    public static ErrorResponseBuilder builder() {
        return new ErrorResponseBuilder();
    }

    public ErrorResponseBuilder error(String error) {
        errorResponse.setError(error);
        return this;
    }

    public ErrorResponseBuilder message(String message) {
        errorResponse.setMessage(message);
        return this;
    }

    public ErrorResponseBuilder errorCode(String errorCode) {
        errorResponse.setErrorCode(errorCode);
        return this;
    }

    public ErrorResponseBuilder path(String path) {
        errorResponse.setErrorCode(path);
        return this;
    }

    public ErrorResponseBuilder status(int status) {
        errorResponse.setStatus(status);
        return this;
    }

    public ErrorResponseBuilder validationErrors(List<ValidationError> validationErrors) {
        errorResponse.setValidationErrors(validationErrors);
        return this;
    }

    public ErrorResponseBuilder details(Map<String, Object> detais) {
        errorResponse.setDetails(detais);
        return this;
    }

    public ErrorResponse build() {
        return errorResponse;
    }
}
