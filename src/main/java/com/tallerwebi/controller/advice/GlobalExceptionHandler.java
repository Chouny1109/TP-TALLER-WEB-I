package com.tallerwebi.controller.advice;

import com.tallerwebi.dominio.excepcion.UsuarioNoAutenticadoException;
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

}
