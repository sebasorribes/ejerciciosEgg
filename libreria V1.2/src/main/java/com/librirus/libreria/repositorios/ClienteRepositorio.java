package com.librirus.libreria.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.librirus.libreria.entidades.Cliente;

@Repository
public interface ClienteRepositorio extends JpaRepository<Cliente, String> {

	@Query("SELECT c FROM Cliente c WHERE c.alta = true")
	public List<Cliente> clientesActivos();
}
