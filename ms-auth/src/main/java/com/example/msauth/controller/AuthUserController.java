package com.example.msauth.controller;


import com.example.msauth.dto.AuthUserDto;
import com.example.msauth.entity.AuthUser;
import com.example.msauth.entity.Role;
import com.example.msauth.entity.TokenDto;
import com.example.msauth.service.AuthUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthUserController {
    @Autowired
    private AuthUserService authUserService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthUserDto authUserDto) {

        authUserService.save(authUserDto);
        authUserService.save(authUserDto); // Usar el método por defecto
        return ResponseEntity.ok("User registered successfully with role: CLIENT");
    }



    @PostMapping("/register-admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> registerAdmin(@RequestBody AuthUserDto authUserDto) {
        authUserService.save(authUserDto, Role.ADMIN);
        return ResponseEntity.ok("Admin registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody AuthUserDto authUserDto) {
        TokenDto tokenDto = authUserService.login(authUserDto);
        if (tokenDto == null)
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(tokenDto);
    }

    @PostMapping("/validate")
    public ResponseEntity<TokenDto> validate(@RequestParam String token) {
        TokenDto tokenDto = authUserService.validate(token);
        if (tokenDto == null)
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(tokenDto);
    }
}
