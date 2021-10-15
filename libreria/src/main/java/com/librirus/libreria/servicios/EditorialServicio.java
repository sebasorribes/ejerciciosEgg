package com.librirus.libreria.servicios;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.librirus.libreria.entidades.Editorial;
import com.librirus.libreria.errores.ErrorServicio;
import com.librirus.libreria.repositorios.EditorialRespositorio;

@Service
public class EditorialServicio {

	@Autowired
	EditorialRespositorio er;

	/**
	 * permite guardar una editorial en la base de datos
	 * 
	 * @param nombre
	 * @throws ErrorServicio
	 */
	@Transactional
	public void guardarEditorial(String nombre) throws ErrorServicio {

		comprobarDatos(nombre);

		Editorial editorial = new Editorial();

		editorial.setNombre(nombre);

		er.save(editorial);
	}

	/**
	 * devuelve todas las editoriales en la base de datos
	 * 
	 * @return lista de editoriales
	 */
	@Transactional(readOnly = true)
	public List<Editorial> buscarTodos() {
		return er.findAll();
	}

	/**
	 * modifica los datos de una editorial en la base de datos
	 * 
	 * @param id
	 * @param nombre
	 * @throws ErrorServicio
	 */
	@Transactional
	public void modificarEditorial(String id, String nombre) throws ErrorServicio {
		comprobarDatos(nombre);

		Optional<Editorial> resultado = er.findById(id);
		if (resultado.isPresent()) {
			Editorial editorial = resultado.get();

			editorial.setNombre(nombre);

			er.save(editorial);
		} else {
			throw new ErrorServicio("no se encontro la editorial seleccionada");
		}
	}

	/**
	 * da de baja una editorial en la base de datos
	 * @param id
	 * @throws ErrorServicio
	 */
	@Transactional
	public void baja(String id) throws ErrorServicio {
		Optional<Editorial> resultado = er.findById(id);
		if (resultado.isPresent()) {
			Editorial editorial = resultado.get();
			if (editorial.getAlta()) {
				editorial.setAlta(false);
				er.save(editorial);
			} else {
				throw new ErrorServicio("la editorial ya se encuentra dada de baja");
			}
		} else {
			throw new ErrorServicio("no se encontro la editorial seleccionada");
		}
	}

	/**
	 * da de alta una editorial que fue dada de baja en la base de datos
	 * @param id
	 * @throws ErrorServicio
	 */
	@Transactional
	public void alta(String id) throws ErrorServicio{
		Optional<Editorial> resultado = er.findById(id);
		if (resultado.isPresent()) {
			Editorial editorial = resultado.get();
			if (!editorial.getAlta()) {
				editorial.setAlta(true);
				er.save(editorial);
			} else {
				throw new ErrorServicio("la editorial ya se encuentra dada de alta");
			}
		} else {
			throw new ErrorServicio("no se encontro la editorial seleccionada");
		}
	}
	
	/**
	 * devuelve una editorial buscandola por su ID
	 * @param id
	 * @return editorial
	 * @throws ErrorServicio
	 */
	@Transactional
	public Editorial buscarID(String id) throws ErrorServicio{
		if(id.isEmpty() || id==null) {
			throw new ErrorServicio("no llego ningun ID para buscar una editorial");
		}
		return er.getById(id);
	}
	
	/**
	 * comprueba que el nombre de la editorial no sea nulo o este vacio, en caso que
	 * suceda, lanza una excepcion
	 * 
	 * @param nombre
	 * @throws ErrorServicio
	 */
	public void comprobarDatos(String nombre) throws ErrorServicio {
		if (nombre == null || nombre.isEmpty()) {
			throw new ErrorServicio("no puede guardar una editorial sin nombre");
		}
	}
}
