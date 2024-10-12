package com.orion.exception;

import com.orion.enums.brand.BrandAccess;
import com.orion.enums.model.ModelAccess;
import com.orion.generics.ResponseObject;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

@RestControllerAdvice
@Log4j2
public class GlobalExceptionHandler {
    private static final String COMPANY_NAME_CONSTRAINT = "uk_company_name";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseObject> handleValidationExceptions(MethodArgumentNotValidException ex) {
        StringBuilder errorMessages = new StringBuilder("Validation failed: ");
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errorMessages.append(fieldName).append(" - ").append(errorMessage).append("; ");
        });
        return returnResponseEntity(HttpStatus.BAD_REQUEST, errorMessages.toString());
    }
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ResponseObject> handleRuntimeExceptions(RuntimeException ex) {
        log.error("Runtime exception: ", ex);
        return returnResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred: " + ex.getMessage());
    }
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ResponseObject> handleSQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException ex) {
        if (ex.getMessage().contains(COMPANY_NAME_CONSTRAINT)) {
            return returnResponseEntity(HttpStatus.BAD_REQUEST, "Company name already exists. Please choose a different name.");
        }
        return returnResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected database error occurred.");
    }
    @ExceptionHandler({SQLException.class, DataAccessException.class, SQLIntegrityConstraintViolationException.class})
    public ResponseEntity<ResponseObject> handleSQLException(Exception ex) {
        log.error("SQL Exception: ", ex);
        return returnResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while processing your request. Please try again later.");
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ResponseObject> handleConflictException(ConflictException ex) {
        return returnResponseEntity(HttpStatus.CONFLICT, ex.getErrorCode().getMessageTitleKey());
    }
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ResponseObject> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        String errorMessage = "Invalid request body.";

        if (isClassNameInExceptionMessage(ex, ModelAccess.class)) {
            errorMessage = "Invalid model name. Some accepted model names: " + ModelAccess.getAcceptedModelNames();
        } else if (isClassNameInExceptionMessage(ex, BrandAccess.class)) {
            errorMessage = "Invalid brand name. Some accepted brand names: " + BrandAccess.getAcceptedBrandNames();
        }

        return returnResponseEntity(HttpStatus.BAD_REQUEST, errorMessage);
    }
    @ExceptionHandler({AccessDeniedException.class, AuthenticationException.class})
    public ResponseEntity<ResponseObject> handleUnauthorizedException(Exception ex) {
        log.error("Unauthorized access attempt: ", ex);
        ResponseObject response = new ResponseObject();
        response.setData("Unauthorized access. Please authenticate and try again.");
        response.prepareHttpStatus(HttpStatus.UNAUTHORIZED);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ResponseObject> handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex) {
        ResponseObject response = new ResponseObject();
        log.error("HTTP method not allowed: ", ex);
        response.setData("This request method is not supported.");
        response.prepareHttpStatus(HttpStatus.METHOD_NOT_ALLOWED);
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(response);
    }

    private boolean isClassNameInExceptionMessage(HttpMessageNotReadableException ex, Class<?> clazz) {
        return ex.getMessage() != null && ex.getMessage().contains(clazz.getName());
    }

    private ResponseEntity<ResponseObject> returnResponseEntity(HttpStatus status, String message) {
        ResponseObject response = new ResponseObject();
        response.setData(message);
        response.prepareHttpStatus(status);
        return ResponseEntity.status(status).body(response);
    }
}
