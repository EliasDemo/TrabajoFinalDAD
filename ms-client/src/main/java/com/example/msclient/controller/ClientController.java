package com.example.msclient.controller;

import com.example.msclient.dto.response.CloudinaryResponse;
import com.example.msclient.entity.Client;
import com.example.msclient.service.ClientService;
import com.example.msclient.service.CloudinaryService;
import com.example.msclient.util.FileUploadUtil;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/Client")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private CloudinaryService cloudinaryService; // Inyectamos el servicio de Cloudinary

    // Método para subir la imagen de un cliente
    @PostMapping("/{id}/upload-image")
    public ResponseEntity<Object> subirImagen(@PathVariable Integer id, @RequestParam("file") MultipartFile file) {
        try {
            // Validar el archivo
            FileUploadUtil.assertAllowed(file, FileUploadUtil.IMAGE_PATTERN);

            // Subir la imagen a Cloudinary
            String fileName = FileUploadUtil.getFileName(file.getOriginalFilename());
            CloudinaryResponse response = cloudinaryService.uploadFile(file, fileName); // Subimos el archivo y obtenemos la URL

            // Obtener el cliente desde el servicio
            Optional<Client> clientOpt = clientService.listarPorId(id);
            if (clientOpt.isPresent()) {
                Client client = clientOpt.get();
                client.setFotousuario(response.getUrl()); // Guardamos la URL de la imagen en el campo `fotousuario`
                clientService.actualizar(client); // Actualizamos el cliente con la nueva URL
            } else {
                return ResponseEntity.notFound().build(); // Si el cliente no existe, retornamos 404
            }

            return ResponseEntity.ok(response); // Devolvemos la respuesta completa de Cloudinary con la URL

        } catch (FileUploadUtil.FileValidationException e) {
            return ResponseEntity.badRequest().body("Error de validación: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al subir la imagen: " + e.getMessage());
        }
    }

    // Método para enviar código de verificación
    @PostMapping("/enviar-codigo")
    public ResponseEntity<String> enviarCodigo(@RequestParam String email) {
        try {
            clientService.enviarCodigoVerificacion(email); // Llamamos al servicio para enviar el código
            return ResponseEntity.ok("Código enviado al correo.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al enviar el código.");
        }
    }

    // Método para verificar código de verificación
    @PostMapping("/verificar-codigo")
    public ResponseEntity<String> verificarCodigo(
            @RequestParam String email,
            @RequestParam String codigo) {
        try {
            // Verificamos si el código ingresado es correcto
            if (clientService.verificarCodigo(email, codigo)) {
                return ResponseEntity.ok("Correo verificado exitosamente.");
            }
            // Si el código es incorrecto, retornamos un error 400
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Código incorrecto.");
        } catch (MessagingException e) {
            // Si ocurre un error relacionado con el envío del correo (por ejemplo, problemas en el servidor)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al enviar el correo de verificación: " + e.getMessage());
        } catch (Exception e) {
            // Captura cualquier otro tipo de error y retorna un error 500 genérico
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al verificar el código: " + e.getMessage());
        }
    }


    // Método para reenviar el código de verificación
    @PostMapping("/reenviar-codigo")
    public ResponseEntity<String> reenviarCodigo(@RequestParam String email) {
        try {
            clientService.reenviarCodigoVerificacion(email); // Llamamos al servicio para reenviar el código
            return ResponseEntity.ok("Nuevo código enviado al correo.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al reenviar el código.");
        }
    }

    // Listar todos los clientes
    @GetMapping()
    public ResponseEntity<List<Client>> list() {
        return ResponseEntity.ok().body(clientService.listar());
    }

    // Obtener un cliente por su ID
    @GetMapping("/{id}")
    public ResponseEntity<Client> findById(@PathVariable Integer id) {
        Optional<Client> clientOpt = clientService.listarPorId(id);
        if (clientOpt.isPresent()) {
            return ResponseEntity.ok().body(clientOpt.get());
        }
        return ResponseEntity.notFound().build(); // Si no se encuentra el cliente, devolvemos 404
    }

    // Guardar un nuevo cliente
    @PostMapping()
    public ResponseEntity<Client> save(@RequestBody Client client) {
        return ResponseEntity.ok().body(clientService.guardar(client));
    }

    // Actualizar un cliente
    @PutMapping()
    public ResponseEntity<Client> update(@RequestBody Client client) {
        return ResponseEntity.ok().body(clientService.actualizar(client));
    }

    // Eliminar un cliente por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id) {
        try {
            clientService.eliminar(id);
            return ResponseEntity.ok("Eliminación correcta");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar el cliente.");
        }
    }
}
