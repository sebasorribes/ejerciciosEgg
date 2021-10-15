package com.librirus.libreria.controladores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.librirus.libreria.entidades.Autor;
import com.librirus.libreria.errores.ErrorServicio;
import com.librirus.libreria.servicios.AutorServicio;

@Controller
@RequestMapping("/autores")
public class AutorControlador {

	@Autowired
	AutorServicio autorServicio;

	/**
	 * devuelve el html "agregar-autor" que contiene un form para colocar los datos del autor
	 * @return
	 */
	@GetMapping("/agregar-autor")
	public String formAutor() {
		return "agregar-autor.html";
	}

	/**
	 * recibe los datos del form para enviarlos al servicio y este se encargue de guardar el autor
	 * en caso de un error muestra nuevamente la pagina con un mensaje de error
	 * @param modelo
	 * @param nombreEditorial
	 * @return html
	 */
	@PostMapping("/registrar")
	public String guardarAutor(ModelMap modelo, @RequestParam String nombreAutor) {
		try {
			autorServicio.guardarAutor(nombreAutor);
		} catch (ErrorServicio e) {
			modelo.put("error", e.getMessage());
			return "agregar-autor.html";
		}
		return "redirect:/autores";
	}
	
	/**
	 * permite dar de baja el autor que se haya seleccionado(coloca el valor de alta en false)
	 * en caso de error carga nuevamente la pagina con el mismo html y le agrega un mensaje de error
	 * @param modelo
	 * @param id
	 * @return
	 */
	@GetMapping("/baja/{id}")
	public String darBaja(ModelMap modelo, @PathVariable String id) {
		try {
			autorServicio.baja(id);
			return "redirect:/autores";
		} catch (ErrorServicio e) {
			modelo.put("error", e.getMessage());
			List<Autor> autores=autorServicio.buscarTodos();
			modelo.put("autores", autores);
			return "autores.html";
		}
	}
	
	/**
	 * permite dar de alta el autor que se haya seleccionado(coloca el valor de alta en true)
	 * en caso de error carga nuevamente la pagina con el mismo html y le agrega un mensaje de error
	 * @param modelo
	 * @param id
	 * @return
	 */
	@GetMapping("/alta/{id}")
	public String darAlta(ModelMap modelo, @PathVariable String id) {
		try {
			autorServicio.alta(id);
			return "redirect:/autores";
		} catch (ErrorServicio e) {
			modelo.put("error", e.getMessage());
			return "autores.html";
		}
	}
	
	/**
	 * carga un form con los datos del autor para que estos puedan ser cambiados
	 * @param modelo
	 * @param id
	 * @return
	 */
	@GetMapping("/modificar/{id}")
	public String modificarAutor(ModelMap modelo, @PathVariable String id) {
		try {
			modelo.put("id", id);
			Autor autor=autorServicio.buscarId(id);
			modelo.put("nombreA", autor.getNombre());
		} catch (ErrorServicio e) {
			return "redirect:/autores";
		}
		
		return "modificarae.html";
	}
	
	/**
	 * utiliza el servicio con los datos modificados para cambiarlos en la base de datos
	 * @param modelo
	 * @param id
	 * @param nombre
	 * @return
	 */
	@PostMapping("/modificar/hecho")
	public String modAutor(ModelMap modelo, @RequestParam String id,@RequestParam String nombre) {
		try {
			autorServicio.modificarAutor(id, nombre);
			return "redirect:/autores";
		} catch (ErrorServicio e) {
			modelo.put("error", e.getMessage());
			modelo.put("id", id);
			return "modificarae.html";
		}
	}
}
