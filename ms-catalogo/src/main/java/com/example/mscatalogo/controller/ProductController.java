package com.example.mscatalogo.controller;

import com.example.mscatalogo.entity.Category;
import com.example.mscatalogo.entity.Product;
import com.example.mscatalogo.service.CategoryService;
import com.example.mscatalogo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Product")
public class ProductController {
    @Autowired
    private ProductService productService;

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
}