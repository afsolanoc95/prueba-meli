package com.hackerrank.sample.controller;

import com.hackerrank.sample.dto.payload.JwtResponse;
import com.hackerrank.sample.dto.payload.LoginRequest;
import com.hackerrank.sample.model.BlacklistedToken;
import com.hackerrank.sample.repository.BlacklistedTokenRepository;
import com.hackerrank.sample.security.JwtUtils;
import com.hackerrank.sample.security.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final BlacklistedTokenRepository blacklistedTokenRepository;
    private final JwtUtils jwtUtils;

    @Operation(summary = "Iniciar sesión", description = "Autentica un usuario y devuelve un token JWT.")
    @PostMapping("/login")
    public ResponseEntity<Object> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        log.debug("Attempting to authenticate user: {}", loginRequest.getUsername());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserPrincipal userDetails = (UserPrincipal) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority()).toList();

        log.info("User logged in successfully: {}", userDetails.getUsername());

        return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), roles));
    }

    @Operation(summary = "Cerrar sesión", description = "Agrega el token JWT actual a la lista negra para invalidarlo.")
    @PostMapping("/logout")
    public ResponseEntity<String> logoutUser(@RequestHeader("Authorization") String tokenHeader) {
        if (tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
            String token = tokenHeader.substring(7);
            Date expiryDate = jwtUtils.getExpirationDateFromJwtToken(token);

            BlacklistedToken blacklistedToken = BlacklistedToken.builder().token(token)
                    .expiryDate(expiryDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()).build();

            blacklistedTokenRepository.save(blacklistedToken);
            log.info("User logged out, token blacklisted.");
        }
        return ResponseEntity.ok("Log out successful!");
    }
}
