package com.example.msauth.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class AuthUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String userName;
    private String password;
    @Enumerated(EnumType.STRING) // Define el rol como un enum
    @Column(nullable = false)
    private Role role; // Campo para distinguir entre ADMIN y CLIENT
}

