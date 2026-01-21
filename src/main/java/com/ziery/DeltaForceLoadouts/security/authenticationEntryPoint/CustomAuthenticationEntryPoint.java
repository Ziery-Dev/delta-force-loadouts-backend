package com.ziery.DeltaForceLoadouts.security.authenticationEntryPoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException {

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json;charset=UTF-8");

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("status", 401);
        errorResponse.put("timestamp", LocalDateTime.now().toString());
        errorResponse.put("path", request.getRequestURI());

        String message;

        String exceptionMessage = authException.getMessage();

        if (exceptionMessage.contains("Full authentication is required")) {
            message = "Token ausente ou inválido.";
        }
        else if (exceptionMessage.contains("JWT expired")
                || exceptionMessage.contains("expired")) {
            message = "Token expirado.";
        }
        else {
            message = "Falha na autenticação.";
        }

        errorResponse.put("erro", message);

        response.getWriter().write(
                objectMapper.writeValueAsString(errorResponse)
        );
    }
}
