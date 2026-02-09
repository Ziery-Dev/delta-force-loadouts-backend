package com.ziery.DeltaForceLoadouts.security.jwt;


import com.ziery.DeltaForceLoadouts.security.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;


    //  Extrai o username (subject) contido no token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    //  Método genérico para extrair qualquer dado (claim) do token
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    //  Gera um token JWT contendo username e data de expiração
    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("role", user.getRole()); // <-- para o FRONT
        claims.put("authorities", List.of("ROLE_" + user.getRole())); // <-- para o BACK
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 10 * 60)) // 1 hora
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }



    //  Valida o token: username igual e não expirado
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    //  Verifica se o token expirou
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    //  Retorna a data de expiração
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    //  Extrai todos os dados (claims) do token
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    //  Converte a chave secreta para formato HMAC-SHA
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
