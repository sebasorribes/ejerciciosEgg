package com.librirus.libreria.servicios;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.librirus.libreria.entidades.Autor;
import com.librirus.libreria.errores.ErrorServicio;
import com.librirus.libreria.repositorios.AutorRepositorio;

@Service
public class AutorServicio {

	@Autowired
	AutorRepositorio ar;

	/**
	 * permite guardar un autor en la base de datos
	 * 
	 * @param nombre
	 * @throws ErrorServicio
	 */
	@Transactional
	public void guardarAutor(String nombre) throws ErrorServicio {

		comprobarDatos(nombre);

		Autor autor = new Autor();
		autor.setNombre(nombre);

		ar.save(autor);
	}

	/**
	 * devuelve una lista con todos los autores en la base de datos
	 * 
	 * @return lista de autores
	 */
	@Transactional(readOnly = true)
	public List<Autor> buscarTodos() {
		return ar.findAll();
	}

	/**
	 * modifica un autor en la base de datos
	 * 
	 * @param id
	 * @param nombre
	 * @throws ErrorServicio
	 */
	@Transactional
	public void modificarAutor(String id, String nombre) throws ErrorServicio {
		comprobarDatos(nombre);

		Optional<Autor> resultado = ar.findById(id);
		if (resultado.isPresent()) {
			Autor autor = resultado.get();
			autor.setNombre(nombre);
			ar.save(autor);
		} else {
			throw new ErrorServicio("no se encontro el autor seleccionado");
		}
	}

	/**
	 * da de baja un autor en la base de datos
	 * 
	 * @param id
	 * @throws ErrorServicio
	 */
	@Transactional
	public void baja(String id) throws ErrorServicio {
		Optional<Autor> resultado = ar.findById(id);
		if (resultado.isPresent()) {
			Autor autor = resultado.get();
			if (autor.getAlta()) {
				autor.setAlta(false);
				ar.save(autor);
			} else {
				throw new ErrorServicio("ese autor ya esta fue dado de baja");
			}
		} else {
			throw new ErrorServicio("no se encontro el autor seleccionado");
		}
	}

	/**
	 * da de alta un autor que fue dado de baja en la base de datos
	 * 
	 * @param id
	 * @throws ErrorServicio
	 */
	@Transactional
	public void alta(String id) throws ErrorServicio {
		Optional<Autor> resultado = ar.findById(id);
		if (resultado.isPresent()) {
			Autor autor = resultado.get();
			if (!autor.getAlta()) {
				autor.setAlta(true);
				ar.save(autor);
			} else {
				throw new ErrorServicio("ese autor ya esta dado de alta");
			}
		} else {
			throw new ErrorServicio("no se encontro el autor seleccionado");
		}
	}

	/**
	 * devuelve un autor buscandolo por su ID
	 * @param id
	 * @return autor
	 * @throws ErrorServicio
	 */
	@Transactional
	public Autor buscarId(String id) throws ErrorServicio{
		if(id.isEmpty() || id==null) {
			throw new ErrorServicio("no se envio un id");
		}
		return ar.getById(id);
	}
	
	/**
	 * comprueba que el nombre del autor no sea nulo o este en blanco, en caso que
	 * suceda, lanza una excepcion
	 * 
	 * @param nombre
	 * @throws ErrorServicio
	 */
	public void comprobarDatos(String nombre) throws ErrorServicio {
		if (nombre == null || nombre.isEmpty()) {
			throw new ErrorServicio("no puede guardar un autor sin nombre");
		}
	}
}
