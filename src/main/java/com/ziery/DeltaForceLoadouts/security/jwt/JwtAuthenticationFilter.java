package com.ziery.DeltaForceLoadouts.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;



    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        //Lê o cabeçalho de autorização
        final String authHeader = request.getHeader("Authorization");



        // Se não houver token ou não começar com "Bearer ", pula o filtro e o responsavel por barrar ou não será os filtros do securityConfig
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        if (request.getServletPath().contains("/user")) {
            filterChain.doFilter(request, response);
            return;
        }

        //  Extrai o token sem o prefixo "Bearer "
        final String jwt = authHeader.substring(7);
        final String username = jwtService.extractUsername(jwt); // 🧠 Lê o usuário do token

        // Se o usuário ainda não estiver autenticado no contexto:
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            //  Valida o token (assinatura e expiração)
            if (jwtService.isTokenValid(jwt, userDetails)) {
                // Cria o objeto de autenticação e adiciona no contexto
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        //  Segue o fluxo normal da requisição
        filterChain.doFilter(request, response);
    }
}
