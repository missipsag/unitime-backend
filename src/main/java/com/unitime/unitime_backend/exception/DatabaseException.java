package com.unitime.unitime_backend.exception;

import com.fasterxml.jackson.databind.ser.Serializers;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DatabaseException  extends BaseException {

    public DatabaseException(String message) {
        super(message, "DATABASE_ERROR", 500);
    }

    public DatabaseException(String message, Throwable cause) {
        super(message, "DATABASE_ERROR", 500, cause);
    }
}
