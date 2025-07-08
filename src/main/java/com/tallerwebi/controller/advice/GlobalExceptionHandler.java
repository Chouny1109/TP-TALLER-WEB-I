package com.tallerwebi.controller.advice;

import com.tallerwebi.dominio.excepcion.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UsuarioNoAutenticadoException.class)
    public ResponseEntity<Map<String, String>> handleUsuarioNoAutenticado(UsuarioNoAutenticadoException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(MisionNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleMisionNotFoundException(MisionNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(MisionNoPerteneceAlUsuarioException.class)
    public ResponseEntity<Map<String, String>> handleMisionNoPerteneceAlUsuarioException(MisionNoPerteneceAlUsuarioException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(MisionYaFinalizadaException.class)
    public ResponseEntity<Map<String, String>> handleMisionYaFinalizadaException(MisionYaFinalizadaException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(MonedasInsuficientesException.class)
    public ResponseEntity<Map<String, String>> handleMonedasInsuficientesException(MonedasInsuficientesException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Map.of("error", ex.getMessage()));
    }

}
