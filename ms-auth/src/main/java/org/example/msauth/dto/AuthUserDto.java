package org.example.msauth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.msauth.enums.Role;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class AuthUserDto {
    private String userName;
    private String password;
    private Role role; // Campo para rol

}
