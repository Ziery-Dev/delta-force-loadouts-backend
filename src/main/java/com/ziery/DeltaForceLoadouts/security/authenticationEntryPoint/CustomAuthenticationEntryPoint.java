package com.ziery.DeltaForceLoadouts.security.authenticationEntryPoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("status", 401);
        errorResponse.put("timestamp", LocalDateTime.now().toString());

        // Define mensagens mais amigáveis
        if (authException.getMessage().contains("Bad credentials")) {
            errorResponse.put("error", "Usuário ou senha inválidos.");
        } else if (authException.getMessage().contains("Full authentication is required")) {
            errorResponse.put("error", "Token ausente ou inválido.");
        } else {
            errorResponse.put("error", "Falha na autenticação.");
        }

        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
