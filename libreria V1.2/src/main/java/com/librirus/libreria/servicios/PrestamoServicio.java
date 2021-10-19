package com.librirus.libreria.servicios;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.librirus.libreria.entidades.Cliente;
import com.librirus.libreria.entidades.Libro;
import com.librirus.libreria.entidades.Prestamo;
import com.librirus.libreria.errores.ErrorServicio;
import com.librirus.libreria.repositorios.PrestamoRepositorio;

@Service
public class PrestamoServicio {

	@Autowired
	PrestamoRepositorio prestamoRepositorio;
	@Autowired
	LibroServicio libroServicio;
	@Autowired
	ClienteServicio clienteServicio;

	/**
	 * recibe el id de libro, el de cliente y realiza la entrada del prestamo,
	 * utilizando el metodo del servicio de libros para modificar la cantidad de
	 * libros
	 * 
	 * @param idLibro
	 * @param idCliente
	 * @throws ErrorServicio
	 */
	@Transactional
	public void guardar(String idLibro, String idCliente) throws ErrorServicio {
		if (libroServicio.comprobacionPrestamo(idLibro)) {
			libroServicio.PrestamosDevoluciones(idLibro, 1);
			
			Libro libro = libroServicio.buscarId(idLibro);
			Cliente cliente = clienteServicio.buscarID(idCliente);
			Date prestado = new Date();
			Prestamo prestamo = new Prestamo();

			prestamo.setFechaPrestamo(prestado);
			prestamo.setLibro(libro);
			prestamo.setCliente(cliente);

			prestamoRepositorio.save(prestamo);
		}
	}

	/**
	 * metodo que permite modificar el libro o autor de un prestamo por si se
	 * selecciono mal
	 * 
	 * @param idLibro
	 * @param idCliente
	 * @throws ErrorServicio
	 */
	@Transactional
	public void modificar(String idPrestamo, String idLibro, String idCliente) throws ErrorServicio {
		Prestamo prestamo = prestamoID(idPrestamo);
		Cliente cliente = clienteServicio.buscarID(idCliente);

		prestamo.setCliente(cliente);
		
		if (!prestamo.getLibro().getId().equals(idLibro)) {
			if(libroServicio.comprobacionPrestamo(idLibro) && libroServicio.comprobacionDevolucion(prestamo.getLibro().getId())) {
				libroServicio.PrestamosDevoluciones(idLibro, 1);
				libroServicio.PrestamosDevoluciones(prestamo.getLibro().getId(), 2);
				Libro libro=libroServicio.buscarId(idLibro);
				prestamo.setLibro(libro);
			}
		}
		prestamoRepositorio.save(prestamo);
	}

	/**
	 * permite realizar la devolucion de un libro y realizar la baja un prestamo
	 * @param idPrestamo
	 * @throws ErrorServicio
	 */
	@Transactional
	public void devolucion(String idPrestamo) throws ErrorServicio{
		Prestamo prestamo=prestamoID(idPrestamo);
		
		if(prestamo.getAlta()) {
		if(libroServicio.comprobacionDevolucion(prestamo.getLibro().getId())) {
			libroServicio.PrestamosDevoluciones(prestamo.getLibro().getId(), 2);
			Date fechadevuelto=new Date();
			prestamo.setFechaDevolucion(fechadevuelto);
			prestamo.setAlta(false);
			
			prestamoRepositorio.save(prestamo);
		}
		}else {
			throw new ErrorServicio("ya se realizo la devolucion del prestamo");
		}
	}
	
	/**
	 * busca y retorna un prestamo por su ID
	 * 
	 * @param id
	 * @return Prestamo
	 * @throws ErrorServicio
	 */
	@Transactional
	public Prestamo prestamoID(String id) throws ErrorServicio {
		Optional<Prestamo> resultado = prestamoRepositorio.findById(id);
		if (resultado.isPresent()) {
			Prestamo prestamo = resultado.get();
			return prestamo;
		} else {
			throw new ErrorServicio("no se encontro un prestamo con ese ID");
		}
	}
	
	/**
	 * metodo que devuelve una lista con todos los prestamos en la DB
	 * @return List<Prestamo>
	 */
	@Transactional(readOnly = true)
	public List<Prestamo> listaPrestamos(){
		return prestamoRepositorio.findAll();
	}
}
