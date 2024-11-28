package com.example.mscatalogo.controller;

import com.example.mscatalogo.entity.Category;
import com.example.mscatalogo.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/categories") // Renombramos la ruta base
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    // Endpoint para listar todas las categorías
    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories(){
        return ResponseEntity.ok().body(categoryService.listar());
    }

    // Endpoint para obtener una categoría por su ID
    @GetMapping("/{categoryId}")
    public ResponseEntity<Category> getCategoryById(@PathVariable("categoryId") Integer id){
        Optional<Category> category = categoryService.buscarPorId(id);
        if (category.isPresent()) {
            return ResponseEntity.ok().body(category.get());
        }
        return ResponseEntity.notFound().build(); // Manejo de error si no se encuentra la categoría
    }

    // Endpoint para guardar una nueva categoría
    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody Category category){
        return ResponseEntity.status(201).body(categoryService.guardar(category));
    }

    // Endpoint para actualizar una categoría existente
    @PutMapping("/{categoryId}")
    public ResponseEntity<Category> updateCategory(@PathVariable("categoryId") Integer id, @RequestBody Category category){
        category.setId(id);
        return ResponseEntity.ok().body(categoryService.actualizar(category));
    }

    // Endpoint para eliminar una categoría por su ID
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable("categoryId") Integer id){
        categoryService.eliminarPorId(id);
        return ResponseEntity.ok("Categoría eliminada correctamente");
    }
}
