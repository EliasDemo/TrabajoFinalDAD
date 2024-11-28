package com.example.msclient.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Data
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String fotousuario; //almacenará la URL de la foto del usuario
    private String name; //nombre del cliente
    private String document; //documento de identidad
    private String correoElectronico; //correo electrónico del cliente
    @Column(length = 6)
    private String codigoVerificacion; //código de verificación para validar la cuenta
    private Boolean emailVerificado = false; //indica si el correo electrónico está verificado

    // Otros métodos o validaciones si son necesarias.
}
