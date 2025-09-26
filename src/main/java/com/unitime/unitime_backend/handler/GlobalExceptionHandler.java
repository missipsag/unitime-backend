package com.unitime.unitime_backend.handler;

import com.unitime.unitime_backend.dto.error.ErrorResponseBuilder;
import com.unitime.unitime_backend.dto.error.ValidationError;
import com.unitime.unitime_backend.exception.BaseException;
import com.unitime.unitime_backend.exception.DatabaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import com.unitime.unitime_backend.dto.error.ErrorResponse;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import  org.springframework.validation.BindException;

import java.sql.SQLException;
import java.util.List;
import jakarta.validation.ConstraintViolationException;
import org.springframework.security.core.AuthenticationException;
import jakarta.validation.ConstraintViolation;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;


import java.util.Map;
import java.util.stream.Collectors;


@RestControllerAdvice
public class GlobalExceptionHandler {
    private  static final Logger log =  LoggerFactory.getLogger(GlobalExceptionHandler.class);

    //Handle custom business exception
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> handleBaseException(BaseException ex, WebRequest request) {
        log.warn("Business exception : {}", ex.getMessage(), ex);

        ErrorResponse errorResponse = ErrorResponseBuilder.builder()
                    .error(HttpStatus.valueOf(ex.getHttpStatus()).getReasonPhrase())
                        .message(ex.getMessage())
                        .errorCode(ex.getErrorCode())
                        .status(ex.getHttpStatus())
                        .path(getPath(request))
                        .build();
        return ResponseEntity.status(ex.getHttpStatus()).body(errorResponse);

    }

    // handle validation errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex,
            WebRequest request
    ) {
        log.warn("Validation error : {}" , ex.getMessage());

        List<ValidationError> validationErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map( error -> new ValidationError(
                        error.getField(),
                        error.getDefaultMessage(),
                        error.getRejectedValue()
                )).toList();

        ErrorResponse errorResponse = ErrorResponseBuilder.builder()
                .error("Validation failed")
                .message("Invalid input parameters")
                .errorCode("VALIDATION_ERROR")
                .status(400)
                .path(getPath(request))
                .validationErrors(validationErrors)
                .build();

        return ResponseEntity.badRequest().body(errorResponse);
    }

    // handle bind exceptions from data validations
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponse> handleBindException(
            BindException ex,
            WebRequest request
    ) {

        log.warn("Bind exception : {}", ex.getMessage());

        List<ValidationError> validationErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new ValidationError(
                        error.getDefaultMessage(),
                        error.getField(),
                        error.getRejectedValue()
                )).toList();

        ErrorResponse errorResponse = ErrorResponseBuilder.builder()
                .error("Validation failed")
                .message("Invalid form data")
                .errorCode("VALIDATION_ERROR")
                .status(400)
                .path(getPath(request))
                .validationErrors(validationErrors)
                .build();

        return  ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(
            ConstraintViolationException ex,
            WebRequest request
    ) {
        log.warn("Constraint violation : {}", ex.getMessage());

        List<ValidationError> validationErrors = ex.getConstraintViolations()
                .stream()
                .map(error -> new ValidationError(
                        getPropertyPath(error),
                        error.getMessage(),
                        error.getInvalidValue()
                )).toList();

        ErrorResponse errorResponse = ErrorResponseBuilder.builder()
                .error("Constraint validation failed")
                .message("Constraint violation occurred")
                .errorCode("CONSTRAINT_VALIDATION_ERROR")
                .status(400)
                .path(getPath(request))
                .validationErrors(validationErrors)
                .build();

        return ResponseEntity.badRequest().body(errorResponse);
    }

    // handle auth exception
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(
            AuthenticationException ex,
            WebRequest request
    ){
        log.warn("Authentication error : {}", ex.getMessage());

        ErrorResponse errorResponse = ErrorResponseBuilder.builder()
                .error("Unauthorized")
                .message("Authentication failed")
                .errorCode("AUTHENTICATION_ERROR")
                .status(401)
                .path(getPath(request))
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    // handle access denied exceptions
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(
            AccessDeniedException ex,
            WebRequest request
    ) {
        log.warn("Access denied : {}", ex.getMessage());

        ErrorResponse errorResponse = ErrorResponseBuilder.builder()
                .error("Forbidden")
                .message("Access denied")
                .errorCode("ACCESS_DENIED")
                .status(403)
                .path(getPath(request))
                .build();

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    @ExceptionHandler({DataAccessException.class, SQLException.class})
    public ResponseEntity<ErrorResponse> handleDatabaseException(
            Exception ex,
            WebRequest request
    ) {
        log.error("Database error : {}", ex.getMessage(), ex);

        String message = "Data integrity constraint violation";
        String errorCode = "DATABASE_ERROR";

        if(ex instanceof DataIntegrityViolationException) {
            message ="Data intgegrity constraint violation";
            errorCode = "DATA_INTEGRITY_VIOLATION";
        }

        ErrorResponse errorResponse  = ErrorResponseBuilder.builder()
                .error("Internal server error")
                .message(message)
                .errorCode(errorCode)
                .status(500)
                .path(getPath(request))
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    // handle malformed JSON requests
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException ex,
            WebRequest request
    ) {
        log.warn("Malformed JSON request : {}", ex.getMessage());

        ErrorResponse errorResponse = ErrorResponseBuilder.builder()
                .error("Bad request")
                .message("Malformed JSON request")
                .errorCode("MALFORMED_JSON")
                .status(400)
                .path(getPath(request))
                .build();

        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingServletRequestParameterException(
            MissingServletRequestParameterException ex,
            WebRequest request
    ) {
        log.warn("Missing request paramater : {}", ex.getMessage());

        ErrorResponse errorResponse = ErrorResponseBuilder.builder()
                .error("Bad request")
                .message(String.format("Missing required paramter : %s ", ex.getParameterName()))
                .errorCode("MISSING_PARAMETER")
                .status(400)
                .path(getPath(request))
                .details(Map.of("parameterName",ex.getParameterName(), "parameterType" , ex.getParameterType()))
                .build();

        return ResponseEntity.badRequest().body(errorResponse);

    }

    // handle method argument type mismatch
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException ex,
            WebRequest request
    ) {
        log.warn("Method argument type mismatch : {}", ex.getMessage());

        ErrorResponse errorResponse = ErrorResponseBuilder.builder()
                .error("Bad request")
                .message(String.format("Invalid value for parameter : %s", ex.getName()))
                .errorCode("INVALID_PARAMETER_TYPE")
                .status(400)
                .path(getPath(request))
                .details(Map.of(
                        "parameterName", ex.getName(),
                        "providedValue", ex.getValue(),
                        "encryptedType", ex.getRequiredType().getSimpleName()
                )).build();
        return ResponseEntity.badRequest().body(errorResponse);
    }

    // handle unsupported http method
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException ex,
            WebRequest request
    ) {
        log.warn("Method not supported : {}", ex.getMessage());

        ErrorResponse errorResponse = ErrorResponseBuilder.builder()
                .error("Method not allowed")
                .message(String.format("Method %s not supported for this endpoint", ex.getMethod()))
                .errorCode("METHOD_NOT_SUPPORTED")
                .status(405)
                .path(getPath(request))
                .details(Map.of("Supported Methods", ex.getSupportedMethods()))
                .build();

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(errorResponse);
    }

    // Handle all other exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex, WebRequest request) {
        log.error("Unexpected error: {}", ex.getMessage(), ex);

        ErrorResponse errorResponse = ErrorResponseBuilder.builder()
                .error("Internal Server Error")
                .message("An unexpected error occurred")
                .errorCode("INTERNAL_ERROR")
                .status(500)
                .path(getPath(request))
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    private String getPath(WebRequest request) {
        return  request.getDescription(false).replace("uri","");
    }

    private String getPropertyPath(ConstraintViolation<?> violation) {
        String path = violation.getPropertyPath().toString();
        return path.isEmpty() ? violation.getRootBeanClass().getSimpleName() : path;
    }

}



