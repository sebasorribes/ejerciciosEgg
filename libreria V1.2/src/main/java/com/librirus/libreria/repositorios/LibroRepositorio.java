package com.librirus.libreria.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.librirus.libreria.entidades.Libro;

@Repository
public interface LibroRepositorio extends JpaRepository<Libro, String> {

	@Query("SELECT l FROM Libro l WHERE l.alta = true")
	public List<Libro> libroActivo();
}
