package com.novely.novely.security;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import lombok.RequiredArgsConstructor;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
@SecurityScheme(name = SecurityConfig.SECURITY, type = SecuritySchemeType.HTTP, bearerFormat = "JWT", scheme = "bearer")
public class SecurityConfig {

    public static final String SECURITY = "bearerAuth";

    @Value("${jwt.public.key}")
    private String publicKeyPath;

    @Value("${jwt.private.key}")
    private String privateKeyPath;

    @Bean
    public RSAPublicKey rsaPublicKey() throws Exception {
        String key = Files.readString(Paths.get(publicKeyPath), StandardCharsets.UTF_8);
        String sanitized = key.replace("-----BEGIN PUBLIC KEY-----", "")
                              .replace("-----END PUBLIC KEY-----", "")
                              .replaceAll("\\s+", "");
        byte[] decoded = Base64.getDecoder().decode(sanitized);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decoded);
        return (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(keySpec);
    }

    @Bean
    public RSAPrivateKey rsaPrivateKey() throws Exception {
        String key = Files.readString(Paths.get(privateKeyPath), StandardCharsets.UTF_8);
        String sanitized = key.replace("-----BEGIN PRIVATE KEY-----", "")
                              .replace("-----END PRIVATE KEY-----", "")
                              .replaceAll("\\s+", "");
        byte[] decoded = Base64.getDecoder().decode(sanitized);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decoded);
        return (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(keySpec);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        
        http.csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/users","/users/auth/signup", "/users/auth/login", "/novels", "/chapters", "/comments", "/ratings", "/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
                .requestMatchers(HttpMethod.GET, "/users/*", "/novels/*", "/chapters/*").permitAll()
                .requestMatchers(HttpMethod.PATCH, "/novels/*/views").permitAll()
                .anyRequest().authenticated()
            ).oauth2ResourceServer(conf -> conf.jwt(Customizer.withDefaults()));
        
        return http.build();
            
    }

    @Bean
    JwtDecoder jwtDecoder(RSAPublicKey rsaPublicKey) {
        return NimbusJwtDecoder.withPublicKey(rsaPublicKey).build();
    }

    @Bean
    JwtEncoder jwtEncoder(RSAPublicKey rsaPublicKey, RSAPrivateKey rsaPrivateKey) {
        var jwk = new RSAKey.Builder(rsaPublicKey).privateKey(rsaPrivateKey).build();
        var jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}