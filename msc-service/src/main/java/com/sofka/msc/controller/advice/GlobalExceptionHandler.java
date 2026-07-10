package com.sofka.msc.controller.advice;

import com.sofka.msc.dto.BaseResponseDto;
import com.sofka.msc.exception.ExceptionManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ExceptionManager.class)
    public ResponseEntity<BaseResponseDto<Object>> handleExceptionManager(ExceptionManager ex) {
        log.error("ExceptionManager error: ", ex);
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        if (ex instanceof ExceptionManager.NotValidFieldException ||
            ex instanceof ExceptionManager.EmptyFieldException ||
            ex instanceof ExceptionManager.NotValidFormatException ||
            ex instanceof ExceptionManager.NullEntityException) {
            status = HttpStatus.BAD_REQUEST;
        }

        BaseResponseDto<Object> response = BaseResponseDto.builder()
                .code(status.value())
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponseDto<Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        log.error("Validation error: ", ex);
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());

        BaseResponseDto<Object> response = BaseResponseDto.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message("Error de validación en los campos")
                .errors(errors)
                .build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<BaseResponseDto<Object>> handleConstraintViolationException(ConstraintViolationException ex) {
        log.error("Constraint violation error: ", ex);
        List<String> errors = ex.getConstraintViolations()
                .stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.toList());

        BaseResponseDto<Object> response = BaseResponseDto.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message("Error de validación de parámetros")
                .errors(errors)
                .build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponseDto<Object>> handleGeneralException(Exception ex) {
        log.error("Unhandled exception error: ", ex);
        BaseResponseDto<Object> response = BaseResponseDto.builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message("Ocurrió un error interno en el servidor")
                .build();
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
