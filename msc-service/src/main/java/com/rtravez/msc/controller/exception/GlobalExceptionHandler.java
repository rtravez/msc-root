package com.rtravez.msc.controller.exception;

import com.rtravez.msc.exception.ExceptionManager;
import lombok.extern.slf4j.Slf4j;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.validation.ConstraintViolationException;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ExceptionManager.class)
    public ResponseEntity<ProblemDetail> handleExceptionManager(ExceptionManager ex) {
        log.error("ExceptionManager error: ", ex);
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        if (ex instanceof ExceptionManager.NotFoundException) {
            status = HttpStatus.NOT_FOUND;
        } else if (ex instanceof ExceptionManager.NotValidFieldException ||
            ex instanceof ExceptionManager.EmptyFieldException ||
            ex instanceof ExceptionManager.NotValidFormatException ||
            ex instanceof ExceptionManager.NullEntityException) {
            status = HttpStatus.BAD_REQUEST;
        }

        return problemDetail(status, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleValidationExceptions(MethodArgumentNotValidException ex) {
        log.error("Validation error: ", ex);
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage()).toList();

        return problemDetail(HttpStatus.BAD_REQUEST, "Error de validación en los campos", errors);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ProblemDetail> handleConstraintViolationException(ConstraintViolationException ex) {
        log.error("Constraint violation error: ", ex);
        List<String> errors = ex.getConstraintViolations()
                .stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage()).toList();

        return problemDetail(HttpStatus.BAD_REQUEST, "Error de validación de parámetros", errors);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ProblemDetail> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        log.warn("Data integrity violation", ex);
        return problemDetail(HttpStatus.CONFLICT,
                "La cuenta ya existe o los datos violan una restricción de integridad");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleGeneralException(Exception ex) {
        log.error("Unhandled exception error: ", ex);
        return problemDetail(HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrió un error interno en el servidor");
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ProblemDetail> handleAccessDeniedException(AccessDeniedException ex) {
        log.warn("Access denied: {}", ex.getMessage());

        return problemDetail(HttpStatus.FORBIDDEN, "No tienes permisos para realizar esta operación");
    }

    private ResponseEntity<ProblemDetail> problemDetail(HttpStatus status, String detail) {
        return ResponseEntity.status(status).body(ProblemDetail.forStatusAndDetail(status, detail));
    }

    private ResponseEntity<ProblemDetail> problemDetail(HttpStatus status, String detail, List<String> errors) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, detail);
        problemDetail.setProperty("errors", errors);
        return ResponseEntity.status(status).body(problemDetail);
    }
}
