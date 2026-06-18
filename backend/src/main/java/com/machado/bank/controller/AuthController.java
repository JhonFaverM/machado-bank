package com.machado.bank.controller;

import com.machado.bank.dto.RegisterRequest;
import com.machado.bank.security.JwtService;
import com.machado.bank.service.IUserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final IUserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthController(IUserService userService,
                          AuthenticationManager authenticationManager,
                          JwtService jwtService) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    // REGISTER
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {

        userService.register(request);

        return ResponseEntity.ok(Map.of(
                "message", "Usuario registrado correctamente"
        ));
    }

    // LOGIN (nuevo)
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {

        String username = request.get("userName");
        String password = request.get("password");

        //Autenticacion
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        // obtener usuario desde BD
        var user = userService.findByUserName(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // 3. Crear Claims con el Rol
        // Spring Security suele buscar el prefijo "ROLE_" si usas hasRole()
        Map<String, Object> claims = new java.util.HashMap<>();
        claims.put("role", user.getRole().name());

        //Generar token
        String token = jwtService.generateToken(username, claims);

        // 4. Respuesta completa (posterior a angular)
        return ResponseEntity.ok(Map.of(
                "token", token,
                "type", "Bearer",
                "userName", user.getUserName(),
                "role", user.getRole().name()
        ));
    }
}