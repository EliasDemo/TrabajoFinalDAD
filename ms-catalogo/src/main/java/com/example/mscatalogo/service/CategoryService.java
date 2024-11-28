package com.example.mscatalogo.service;

import com.example.mscatalogo.entity.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    public List<Category> listar();
    public Optional<Category> buscarPorId(Integer id);
    public Category guardar(Category category);
    public Category actualizar(Category category);
    public void eliminarPorId(Integer id);
}