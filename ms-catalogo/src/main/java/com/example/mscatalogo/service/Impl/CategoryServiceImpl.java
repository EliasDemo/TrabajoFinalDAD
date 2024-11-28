package com.example.mscatalogo.service.Impl;

import com.example.mscatalogo.entity.Category;
import com.example.mscatalogo.repository.CategoryRepository;
import com.example.mscatalogo.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Override
    public List<Category> listar() {
        return categoryRepository.findAll();
    }

    @Override
    public Optional<Category> buscarPorId(Integer id) {
        return categoryRepository.findById(id);
    }

    @Override
    public Category guardar(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public Category actualizar(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public void eliminarPorId(Integer id) {
        categoryRepository.deleteById(id);
    }
}