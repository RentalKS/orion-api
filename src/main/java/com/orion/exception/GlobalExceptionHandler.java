package com.orion.exception;

import com.orion.enums.BrandAccess;
import com.orion.enums.model.ModelAccess;
import com.orion.generics.ResponseObject;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final String COMPANY_NAME_CONSTRAINT = "uk_company_name";
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeExceptions(RuntimeException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleSQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException ex) {
        ResponseObject response = new ResponseObject();
        if (ex.getMessage().contains(COMPANY_NAME_CONSTRAINT)) {
            response.setData("Company name already exists. Please choose a different name.");
            response.prepareHttpStatus(HttpStatus.BAD_REQUEST);
            return ResponseEntity.status(response.getStatus()).body(response.getData().toString());
        }
        response.setData("An unexpected error occurred.");
        response.prepareHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        return ResponseEntity.status(response.getStatus()).body(response.getData().toString());
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ResponseObject> handleConflictException(ConflictException ex) {
        ResponseObject response = new ResponseObject();
        response.setData(ex.getErrorCode().getMessageTitleKey());
        response.prepareHttpStatus(HttpStatus.CONFLICT);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ResponseObject> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        ResponseObject response = new ResponseObject();

        if (isClassNameInExceptionMessage(ex, ModelAccess.class)) {
            response.setData("Invalid model name. Some Accepted model names: " + ModelAccess.getAcceptedModelNames());
            response.prepareHttpStatus(HttpStatus.BAD_REQUEST);
            return ResponseEntity.status(response.getStatus()).body(response);
        }

        if (isClassNameInExceptionMessage(ex, BrandAccess.class)) {
            response.setData("Invalid brand name. Some Accepted brand names: " + BrandAccess.getAcceptedBrandNames());
            response.prepareHttpStatus(HttpStatus.BAD_REQUEST);
            return ResponseEntity.status(response.getStatus()).body(response);
        }

        response.setData("Invalid request body.");
        response.prepareHttpStatus(HttpStatus.BAD_REQUEST);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    private boolean isClassNameInExceptionMessage(HttpMessageNotReadableException ex, Class<?> clazz) {
        return ex.getMessage() != null && ex.getMessage().contains(clazz.getName());
    }

}