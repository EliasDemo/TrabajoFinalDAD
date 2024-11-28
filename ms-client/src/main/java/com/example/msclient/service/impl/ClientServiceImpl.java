package com.example.msclient.service.impl;

import com.example.msclient.entity.Client;
import com.example.msclient.repository.ClientRepository;
import com.example.msclient.service.ClientService;
import com.example.msclient.service.EmailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private EmailService emailService;

    @Override
    public List<Client> listar() {
        return clientRepository.findAll();
    }

    @Override
    public Optional<Client> listarPorId(Integer id) {
        return clientRepository.findById(id);
    }

    @Override
    public Client guardar(Client client) {
        return clientRepository.save(client);
    }

    @Override
    public Client actualizar(Client client) {
        return clientRepository.save(client);
    }

    @Override
    public void eliminar(Integer id) {
        clientRepository.deleteById(id);
    }

    // Generar un código de verificación
    private String generateVerificationCode() {
        Random random = new Random();
        return String.valueOf(100000 + random.nextInt(900000)); // Genera un código de 6 dígitos
    }

    @Override
    public void enviarCodigoVerificacion(String email) throws MessagingException {
        Optional<Client> optionalClient = clientRepository.findByCorreoElectronico(email); // Buscamos por correo electrónico

        if (optionalClient.isPresent()) {
            Client client = optionalClient.get();
            String codigo = generateVerificationCode(); // Método que genera el código de verificación
            client.setCodigoVerificacion(codigo);
            clientRepository.save(client); // Guardamos el cliente con el código

            // Enviar correo con el código de verificación
            String asunto = "Código de Verificación para tu cuenta";
            String cuerpo = "Tu código de verificación es: " + codigo;
            emailService.sendEmail(email, asunto, cuerpo); // Enviar el correo
        } else {
            throw new RuntimeException("Cliente no encontrado con el correo proporcionado.");
        }
    }

    @Override
    public boolean verificarCodigo(String email, String codigoIngresado) throws MessagingException {
        Optional<Client> optionalClient = clientRepository.findByCorreoElectronico(email); // Usamos el método del repositorio

        if (optionalClient.isPresent()) {
            Client client = optionalClient.get();
            if (codigoIngresado.equals(client.getCodigoVerificacion())) {
                // Verificar y limpiar el código de verificación
                client.setEmailVerificado(true);
                client.setCodigoVerificacion(null); // Limpiar el código
                clientRepository.save(client); // Guardamos el cliente con la verificación

                // Enviar correo de confirmación
                enviarCorreoConfirmacionRegistro(client);
                return true;
            }
        }
        return false; // El código no coincide
    }

    @Override
    public void reenviarCodigoVerificacion(String email) throws MessagingException {
        Optional<Client> optionalClient = clientRepository.findByCorreoElectronico(email); // Buscamos por correo electrónico

        if (optionalClient.isPresent()) {
            Client client = optionalClient.get();
            String nuevoCodigo = generateVerificationCode(); // Generamos un nuevo código
            client.setCodigoVerificacion(nuevoCodigo); // Actualizamos el código
            client.setEmailVerificado(false); // Reiniciar estado de verificación
            clientRepository.save(client); // Guardamos los cambios

            // Enviar el código por correo
            String asunto = "Reenvío de Código de Verificación";
            String cuerpo = "Tu nuevo código de verificación es: " + nuevoCodigo;
            emailService.sendEmail(email, asunto, cuerpo); // Enviar el correo
        } else {
            throw new RuntimeException("Cliente no encontrado con el correo proporcionado.");
        }
    }

    // Método para enviar correo de confirmación de registro (no está implementado completamente)
    private void enviarCorreoConfirmacionRegistro(Client client) throws MessagingException {
        // Lógica para enviar un correo de confirmación
    }
}
