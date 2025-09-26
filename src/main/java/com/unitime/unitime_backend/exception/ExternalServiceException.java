package com.unitime.unitime_backend.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ExternalServiceException extends BaseException{
    public ExternalServiceException(String message) {
        super(message, "EXTERNAL_SERVICE_ERROR", 502);
    }

    public ExternalServiceException(String serviceName ,String message ) {
        super(String.format(" External service : %s error : %s", serviceName, message),"EXTERNAL_SERVICE_ERROR",502);
    }
}
