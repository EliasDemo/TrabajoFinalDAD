package com.example.msauth.service.impl;


import com.example.msauth.dto.AuthUserDto;
import com.example.msauth.entity.AuthUser;
import com.example.msauth.entity.Role;
import com.example.msauth.entity.TokenDto;
import com.example.msauth.repository.AuthUserRepository;
import com.example.msauth.security.JwtProvider;
import com.example.msauth.service.AuthUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;




import java.util.Optional;




@Service
public class AuthUserServiceImpl implements AuthUserService {
    @Autowired
    AuthUserRepository authUserRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    JwtProvider jwtProvider;


    @Override
    public AuthUser save(AuthUserDto authUserDto) {

        Optional<AuthUser> user = authUserRepository.findByUserName(authUserDto.getUserName());
        if (user.isPresent()) {
            throw new RuntimeException("User already exists");
        }

        String password = passwordEncoder.encode(authUserDto.getPassword());

        AuthUser authUser = AuthUser.builder()
                .userName(authUserDto.getUserName())
                .password(password)
                .role(Role.CLIENT)
                .build();

        return authUserRepository.save(authUser);
    }


    @Override
    public AuthUser save(AuthUserDto authUserDto, Role role) {
        Optional<AuthUser> user = authUserRepository.findByUserName(authUserDto.getUserName());
        if (user.isPresent()) {
            throw new RuntimeException("User already exists");
        }
        String password = passwordEncoder.encode(authUserDto.getPassword());


        AuthUser authUser = AuthUser.builder()
                .userName(authUserDto.getUserName())
                .password(password)
                .role(role)
                .build();

        return authUserRepository.save(authUser);
    }



    @Override
    public TokenDto login(AuthUserDto authUserDto) {
        // Busca al usuario por nombre
        AuthUser user = authUserRepository.findByUserName(authUserDto.getUserName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Valida la contraseña
        if (!passwordEncoder.matches(authUserDto.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid credentials");
        }

        // Genera el token JWT
        String token = jwtProvider.generateToken(user);

        return new TokenDto(token);
    }




    @Override
    public TokenDto validate(String token) {
        if (!jwtProvider.validate(token))
            return null;
        String username = jwtProvider.getUserNameFromToken(token);
        if (!authUserRepository.findByUserName(username).isPresent())
            return null;
        return new TokenDto(token);
    }



}
