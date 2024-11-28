package com.example.mscatalogo.controller;

import com.example.mscatalogo.entity.Product;
import com.example.mscatalogo.service.CloudinaryService;
import com.example.mscatalogo.service.ProductService;
import com.example.mscatalogo.util.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import java.util.List;

@RestController
@RequestMapping("/Product")
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private CloudinaryService cloudinaryService;

    @GetMapping()
    public ResponseEntity<List<Product>> list(){
        return ResponseEntity.ok().body(productService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getById(@PathVariable Integer id){
        return ResponseEntity.ok(productService.buscarPorId(id).get());
    }

    @PostMapping()
    public ResponseEntity<Product> save(@RequestBody Product product){
        return ResponseEntity.ok().body(productService.guardar(product));
    }

    @PutMapping()
    public ResponseEntity<Product> update(@RequestBody Product product){
        return ResponseEntity.ok().body(productService.actualizar(product));
    }

    @DeleteMapping("/{id}")
    public String deleteById(@PathVariable(required = true)Integer id){
        productService.eliminarPorId(id);
        return "elminacion correcta";
    }

    @PutMapping("/{id}/reduce-stock")
    public ResponseEntity<Void> reduceStock(@PathVariable Integer id, @RequestParam Integer amount) {
        productService.reduceStock(id, amount);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String code) {

        List<Product> results = productService.advancedSearch(name, category, code);
        return ResponseEntity.ok(results);
    }

    @PutMapping("/{id}/increase-stock")
    public ResponseEntity<Void> increaseStock(@PathVariable Integer id, @RequestParam Integer amount) {
        System.out.println("Increase stock called for Product ID: " + id + " with Amount: " + amount);

        Product product = productService.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        System.out.println("Product found: " + product);

        product.setStock(product.getStock() + amount);
        productService.actualizar(product);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/upload-image")
    public ResponseEntity<Object> subirImagen(@PathVariable Integer id, @RequestParam("file") MultipartFile file) {
        try {
            // Validar el archivo (asegurarse de que sea una imagen permitida)
            FileUploadUtil.assertAllowed(file, FileUploadUtil.IMAGE_PATTERN);

            // Obtener el nombre del archivo y subirlo
            String fileName = FileUploadUtil.getFileName(file.getOriginalFilename());
            // Supongamos que tienes un servicio para subir archivos (Cloudinary, S3, etc.)
            String imageUrl = cloudinaryService.uploadFile(file, fileName).getUrl();

            // Buscar el producto en la base de datos
            Optional<Product> productOpt = productService.buscarPorId(id);
            if (!productOpt.isPresent()) {
                return ResponseEntity.notFound().build(); // Producto no encontrado
            }

            Product product = productOpt.get();
            // Asignar la URL de la imagen al campo fotoproducto
            product.setFotoproducto(imageUrl);

            // Guardar el producto con la URL de la imagen
            productService.actualizar(product);

            return ResponseEntity.ok("Imagen subida correctamente: " + imageUrl);
        } catch (FileUploadUtil.FileValidationException e) {
            return ResponseEntity.badRequest().body("Error de validación: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al subir la imagen: " + e.getMessage());
        }
    }

}