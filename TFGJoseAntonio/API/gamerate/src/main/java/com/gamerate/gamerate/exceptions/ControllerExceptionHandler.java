package com.gamerate.gamerate.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response> handleValidationArgumentsErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        String mapAsString = errors.keySet().stream()
                .map(key -> key + " = " + errors.get(key))
                .collect(Collectors.joining(", ", "{ ", " }"));

        log.warn("Error de validación en los argumentos: {}", mapAsString);

        return new ResponseEntity<>(Response.validationError(mapAsString), HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundEntityException.class)
    public ResponseEntity<Response> handleNotFoundEntityException(NotFoundEntityException ex) {
        log.error("Entidad no encontrada: {}", ex.getMessage());

        return new ResponseEntity<>(
                Response.generalError(HttpStatus.NOT_FOUND.value(), ex.getMessage()),
                HttpStatus.NOT_FOUND
        );
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response> handleException(Exception ex) {
        log.error("¡ERROR CRÍTICO INESPERADO EN EL SISTEMA!", ex);

        return new ResponseEntity<>(
                Response.generalError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error inesperado en el sistema"),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DuplicateEntityException.class)
    public ResponseEntity<Response> handleDuplicateEntityException(DuplicateEntityException ex) {
        log.warn("Intento de duplicidad detectado: {}", ex.getMessage());
        return new ResponseEntity<>(
                Response.generalError(HttpStatus.CONFLICT.value(), ex.getMessage()),
                HttpStatus.CONFLICT
        );
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Response> handleBadCredentials(org.springframework.security.authentication.BadCredentialsException ex) {
        log.warn("Intento de acceso fallido: {}", ex.getMessage());
        return new ResponseEntity<>(
                Response.generalError(HttpStatus.UNAUTHORIZED.value(), "Usuario o contraseña incorrectos"),
                HttpStatus.UNAUTHORIZED
        );
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Response> handleAccessDenied(org.springframework.security.access.AccessDeniedException ex) {
        log.warn("Acceso denegado: {}", ex.getMessage());
        return new ResponseEntity<>(
                Response.generalError(HttpStatus.FORBIDDEN.value(), "No tienes permisos suficientes"),
                HttpStatus.FORBIDDEN
        );
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(InvalidDataException.class)
    public ResponseEntity<Response> handleInvalidDataException(InvalidDataException ex) {
        log.error("Datos inválidos recibidos en el servidor: {}", ex.getMessage());
        return new ResponseEntity<>(
                Response.generalError(HttpStatus.UNPROCESSABLE_ENTITY.value(), ex.getMessage()),
                HttpStatus.UNPROCESSABLE_ENTITY
        );
    }
}
