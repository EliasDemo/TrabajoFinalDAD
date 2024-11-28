package com.example.msauth.service;


import com.example.msauth.dto.AuthUserDto;
import com.example.msauth.entity.AuthUser;
import com.example.msauth.entity.Role;
import com.example.msauth.entity.TokenDto;

public interface AuthUserService {

    public AuthUser save(AuthUserDto authUserDto);


    public AuthUser save(AuthUserDto authUserDto, Role role);


    public TokenDto login(AuthUserDto authUserDto);


    public TokenDto validate(String token);


}

