package com.example.sportmanagerpro.services;



import com.example.sportmanagerpro.models.Categoria;
import com.example.sportmanagerpro.repositories.CategoriaRepository;

import java.util.List;

/**
 * Contiene la lógica de negocio para categorías.
 */
public class CategoriaService {

    private final CategoriaRepository categoriaRepository = new CategoriaRepository();

    public void guardar(Categoria categoria) {
        validarCategoria(categoria);
        categoriaRepository.guardar(categoria);
    }

    public void actualizar(Categoria categoria) {
        validarCategoria(categoria);
        categoriaRepository.actualizar(categoria);
    }

    public List<Categoria> listarTodas() {
        return categoriaRepository.listarTodas();
    }

    public void desactivar(int idCategoria) {
        categoriaRepository.desactivar(idCategoria);
    }

    private void validarCategoria(Categoria categoria) {
        if (categoria.getNombre() == null || categoria.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("Debe ingresar el nombre de la categoría.");
        }
    }
}