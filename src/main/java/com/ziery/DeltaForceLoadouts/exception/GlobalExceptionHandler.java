package com.ziery.DeltaForceLoadouts.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Slf4j

public class GlobalExceptionHandler {

    //  Erros de validação (DTOs com @NotBlank, @NotNull, etc)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors); //400
    }

    //Erro de dados duplicados
    @ExceptionHandler(DadoDuplicadoException.class)
    public ResponseEntity<Map<String, String>> handleDadoDuplicado(DadoDuplicadoException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("erro", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error); // 409
    }

    //Erro de dados não econtrados
    @ExceptionHandler(DadoNaoEncontradoException.class)
    public ResponseEntity<Map<String, String>> handleDadoNaoEcontrado(DadoNaoEncontradoException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("erro", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error); // 404
    }

    //Erro de acesso negado
    @ExceptionHandler(AcessoNegadoException.class)
    public ResponseEntity<Map<String, String>> handleAcessoNegado(AcessoNegadoException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("erro", ex.getMessage());
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(error); // 405
    }
    // Erros de regra de negócio
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("erro", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error); //400
    }

    //Erro relacionado a excesso de tentativas num determinado tempo
    @ExceptionHandler(RateLimitException.class)
    public ResponseEntity<Map<String, String>> handleRateLimit(RateLimitException ex) {
        return ResponseEntity
                .status(HttpStatus.TOO_MANY_REQUESTS)
                .body(Map.of("erro", ex.getMessage()));
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGenericException(Exception ex) {

        log.error("Erro interno não tratado", ex);

        Map<String, String> error = new HashMap<>();
        error.put("erro", "Erro interno no servidor");

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(error);
    }


}