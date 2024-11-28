package com.example.msclient.repository;

import com.example.msclient.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Integer> {

    // Método para buscar un candidato por su correo electrónico
    Optional<Client> findByCorreoElectronico(String correoElectronico);
}
