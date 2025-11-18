package com.ziery.DeltaForceLoadouts.security.config;

import com.ziery.DeltaForceLoadouts.security.authenticationEntryPoint.CustomAuthenticationEntryPoint;
import com.ziery.DeltaForceLoadouts.security.jwt.JwtAuthenticationFilter;
import com.ziery.DeltaForceLoadouts.security.userDetails.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final CustomAuthenticationEntryPoint customEntryPoint;
    private final UserDetailsServiceImpl userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                //  Desativa CSRF (não é necessário em APIs REST)
                .csrf(AbstractHttpConfigurer::disable)
                // Define endpoints públicos e os demais somente autenticado
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers( "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/users", "/arma", "/arma/**", "/operador", "/operador/**",  "/build", "/build/**" ).permitAll()
                        .requestMatchers(HttpMethod.POST, "/arma", "/arma/**", "/operador", "/operador/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                //  Define como os erros de autenticação serão tratados
                //garante que, se o token estiver faltando ou inválido, a API retorne um erro JSON limpo (e não a tela de login padrão do Spring Security).
                //.exceptionHandling(ex -> ex.authenticationEntryPoint(customEntryPoint))


                // Define que a aplicação não manterá estado de sessão (stateless)
                //STATELESS, você diz ao Spring Security para ignorar cookies de sessão, garantindo que apenas o JWT seja usado para identificar o usuário.
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(Customizer.withDefaults())
                //Este método registra um componente que sabe como autenticar um usuário.
                .authenticationProvider(authenticationProvider())

                //  Adiciona o filtro JWT antes do filtro padrão de login
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    //Cors config
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:8081")); // ou "*"
        configuration.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }




}
