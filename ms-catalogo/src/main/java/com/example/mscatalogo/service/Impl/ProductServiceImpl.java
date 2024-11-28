package com.example.mscatalogo.service.Impl;

import com.example.mscatalogo.entity.Category;
import com.example.mscatalogo.entity.Product;
import com.example.mscatalogo.repository.CategoryRepository;
import com.example.mscatalogo.repository.ProductRepository;
import com.example.mscatalogo.service.CategoryService;
import com.example.mscatalogo.service.ProductService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Override
    public List<Product> listar() {
        return productRepository.findAll();
    }

    @Override
    public Optional<Product> buscarPorId(Integer id) {

        return productRepository.findById(id);
    }

    @Override
    public Product guardar(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Product actualizar(Product product) {
        return productRepository.save(product);
    }

    @Override
    public void eliminarPorId(Integer id) {
        productRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void reduceStock(Integer productId, Integer amount) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));


        if (product.getStock() < amount) {
            throw new RuntimeException("Insufficient stock for product " + product.getName());
        }

        // Reducir el stock en la base de datos
        product.setStock(product.getStock() - amount);
        productRepository.save(product);
    }

//filtros de busqueda o busqueda avanzad

    @Override
    public List<Product> searchByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name);
    }

    @Override
    public List<Product> searchByCategory(String category) {
        return productRepository.findByCategoryNameIgnoreCase(category);
    }

    @Override
    public List<Product> searchByCode(String code) {
        return productRepository.findByCode(code);
    }

    @Override
    public List<Product> advancedSearch(String name, String category, String code) {
        if (name != null && !name.isEmpty()) {
            return searchByName(name);
        }
        if (category != null && !category.isEmpty()) {
            return searchByCategory(category);
        }
        if (code != null && !code.isEmpty()) {
            return searchByCode(code);
        }
        return productRepository.findAll();
    }
}