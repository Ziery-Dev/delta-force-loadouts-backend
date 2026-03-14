package com.ziery.DeltaForceLoadouts.exception;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
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
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error); // 403
    }

    //Erro de recurso em uso que geralemnte não pode excluido ou alterado
    @ExceptionHandler(RecursoEmUsoException.class)
    public ResponseEntity<Map<String, String>> handleRecursoEmUsoException(RecursoEmUsoException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("erro", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error); // 403
    }

    //Geralmente uma url digitada errada ou mesmo estando certa mas o metodo não existe para ela, cai aqui
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Map<String, String>> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("erro", "Método HTTP não suportado para este endpoint");
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(error); // 405
    }
    //Erro de limite de tentativas de determinada requisição
    @ExceptionHandler(RateLimitException.class)
    public ResponseEntity<Map<String, String>> habdleRateLimitException(RateLimitException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("erro", ex.getMessage());
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(error); // 429
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGenericException(Exception ex) {
        log.error("Erro interno não tratado", ex);
        Map<String, String> error = new HashMap<>();
        error.put("erro", "Erro interno no servidor");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error); // 500
    }


}