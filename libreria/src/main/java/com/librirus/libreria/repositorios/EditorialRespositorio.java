package com.librirus.libreria.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.librirus.libreria.entidades.Editorial;

@Repository
public interface EditorialRespositorio extends JpaRepository<Editorial, String> {

}
