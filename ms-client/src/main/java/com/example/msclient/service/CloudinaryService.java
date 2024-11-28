package com.example.msclient.service;
import com.cloudinary.Cloudinary;
import com.example.msclient.dto.response.CloudinaryResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service

public class CloudinaryService {

    @Autowired
    private Cloudinary cloudinary;

    @Transactional
    public CloudinaryResponse uploadFile(MultipartFile file, String fileName) {
        try {
            // Subir archivo a Cloudinary
            Map<String, Object> result = cloudinary.uploader().upload(
                    file.getBytes(),
                    Map.of("public_id", "nhndev/product/" + fileName)
            );

            // Obtener información necesaria de la respuesta
            final String url = (String) result.get("secure_url");
            final String publicId = (String) result.get("public_id");

            // Crear y devolver una respuesta personalizada
            return CloudinaryResponse.builder()
                    .publicId(publicId)
                    .url(url)
                    .build();

        } catch (Exception e) {
            // Manejo de errores
            throw new RuntimeException("Failed to upload file", e);
        }
    }
}

