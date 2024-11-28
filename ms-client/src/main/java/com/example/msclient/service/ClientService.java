package com.example.msclient.service;

import com.example.msclient.entity.Client;
import jakarta.mail.MessagingException;
import java.util.List;
import java.util.Optional;

public interface ClientService {
    public List<Client> listar(); // Listar todos los clientes
    public Optional<Client> listarPorId(Integer id); // Obtener cliente por ID
    public Client guardar(Client client); // Guardar un cliente
    public Client actualizar(Client client); // Actualizar un cliente
    public void eliminar(Integer id); // Eliminar un cliente

    // Métodos para la verificación de correo
    void enviarCodigoVerificacion(String email) throws MessagingException; // Enviar código de verificación
    boolean verificarCodigo(String email, String codigo) throws MessagingException; // Verificar código de verificación
    void reenviarCodigoVerificacion(String email) throws MessagingException; // Reenviar código de verificación
}
