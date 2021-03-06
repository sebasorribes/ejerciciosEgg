package com.librirus.libreria.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.librirus.libreria.entidades.Autor;

@Repository
public interface AutorRepositorio  extends JpaRepository<Autor, String>{

}
