package com.librirus.libreria.controladores;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.librirus.libreria.entidades.Cliente;
import com.librirus.libreria.entidades.Libro;
import com.librirus.libreria.entidades.Prestamo;
import com.librirus.libreria.errores.ErrorServicio;
import com.librirus.libreria.servicios.ClienteServicio;
import com.librirus.libreria.servicios.LibroServicio;
import com.librirus.libreria.servicios.PrestamoServicio;

@Controller
@RequestMapping("/prestamos")
public class PrestamoControlador {
	
	@Autowired
	PrestamoServicio prestamoServicio;
	@Autowired
	ClienteServicio clienteServicio;
	@Autowired
	LibroServicio libroServicio;
	
	/**
	 * devuelve el html "agregar-prestamo" que contiene un form para colocar el libro que se va a prestar
	 * y el cliente
	 * @param modelo
	 * @return
	 */
	@GetMapping("/prestar")
	public String mostrarFormPrestamo(ModelMap modelo) {
		try {
			List<Libro> libros= libroServicio.librosActivos();
			List<Cliente> clientes=clienteServicio.listaClientesActivos();
			
			modelo.put("libros", libros);
			modelo.put("clientes", clientes);
			return "agregar-prestamo.html";
		} catch (Exception e) {
			return "redirect:/prestamos";
		}
	}
	
	/**
	 * recibe los datos del form para enviarlos al servicio y este se encargue de guardar el prestamo.
	 * en caso de un error, muestra nuevamente la pagina con un mensaje de error
	 * @param modelo
	 * @param libro
	 * @param cliente
	 * @return
	 */
	@PostMapping("/prestar")
	public String realizarPrestamo(ModelMap modelo, @RequestParam String libro,@RequestParam String cliente) {
		try {
			prestamoServicio.guardar(libro, cliente);
			return "redirect:/prestamos";
		}catch(ErrorServicio es) {
			List<Libro> libros= libroServicio.librosActivos();
			List<Cliente> clientes=clienteServicio.listaClientesActivos();
			
			modelo.put("error", es.getMessage());
			modelo.put("libros", libros);
			modelo.put("clientes", clientes);
			return "agregar-prestamo.html";
		}
		catch (Exception e) {
			List<Libro> libros= libroServicio.librosActivos();
			List<Cliente> clientes=clienteServicio.listaClientesActivos();
			
			modelo.put("error", e.getMessage());
			modelo.put("libros", libros);
			modelo.put("clientes", clientes);
			return "agregar-prestamo.html";
		}
	}
	
	/**
	 * muestra un form con los datos del prestamo seleccionado para poder modificarlos.
	 * @param modelo
	 * @param id
	 * @return
	 */
	@GetMapping("/modificar{id}")
	public String formModificarPrestamo(ModelMap modelo,@RequestParam String id) {
		try {
			Prestamo prestamo=prestamoServicio.prestamoID(id);
			List<Libro> libros=libroServicio.librosActivosSinSelecto(prestamo.getLibro());
			List<Cliente> clientes=clienteServicio.listaClientesActivosSinSelecto(prestamo.getCliente());
			
			modelo.put("id", id);
			modelo.put("clienteS", prestamo.getCliente());
			modelo.put("libroS", prestamo.getLibro());
			modelo.put("libros", libros);
			modelo.put("clientes", clientes);
			
			return "modificarPrestamo.html";
		} catch (ErrorServicio e) {
			List<Prestamo> prestamos=prestamoServicio.listaPrestamos();
			modelo.put("prestamos", prestamos);
			modelo.put("error", e.getMessage());
			return "prestamos.html";
		}
		
	}
	
	/**
	 * utiliza el servicio con los datos modificados para cambiarlos en la base de datos.
	 * en caso de algun error, muestra nuevamente la pagina de los prestamos con el mensaje de error.
	 * @param modelo
	 * @param id
	 * @param cliente
	 * @param libro
	 * @return
	 */
	@PostMapping("/modificar{id}")
	public String cambio(ModelMap modelo,@RequestParam String id,@RequestParam String cliente,@RequestParam String libro) {
		try {
			prestamoServicio.modificar(id, libro, cliente);
			return "redirect:/prestamos";
		} catch (ErrorServicio e) {
			List<Prestamo> prestamos=prestamoServicio.listaPrestamos();
			modelo.put("prestamos", prestamos);
			modelo.put("error", e.getMessage());
			return "prestamos.html";
		} catch (Exception ex) {
			List<Prestamo> prestamos=prestamoServicio.listaPrestamos();
			modelo.put("prestamos", prestamos);
			modelo.put("error", ex.getMessage());
			return "prestamos.html";
		}
	}

	/**
	 * devuelve un libro, le asigna una fecha de devolucion (la del dia actual) y colocare el atributo alta en false
	 * en caso de un error, muestra la pagina con los prestamos con el mensaje del error
	 * @param modelo
	 * @param id
	 * @return
	 */
	@GetMapping("/baja{id}")
	public String devolucion(ModelMap modelo, @RequestParam String id) {
		try {
			prestamoServicio.devolucion(id);
			return "redirect:/prestamos";
		} catch (ErrorServicio e) {
			List<Prestamo> prestamos=prestamoServicio.listaPrestamos();
			modelo.put("prestamos", prestamos);
			modelo.put("error", e.getMessage());
			return "prestamos.html";
		} catch (Exception ex) {
			List<Prestamo> prestamos=prestamoServicio.listaPrestamos();
			modelo.put("prestamos", prestamos);
			modelo.put("error", ex.getMessage());
			return "prestamos.html";
		}
	}
}
