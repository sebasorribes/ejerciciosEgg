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
import com.librirus.libreria.entidades.Editorial;
import com.librirus.libreria.errores.ErrorServicio;
import com.librirus.libreria.servicios.EditorialServicio;


@Controller
@RequestMapping("/editoriales")
public class EditorialControlador {

	@Autowired
	EditorialServicio editorialServicio;
	/**
	 * devuelve el html "agregar-editorial" que contiene un form para colocar los datos de la editorial
	 * @return
	 */
	@GetMapping("/agregar-editorial")
	public String formAddEditorial() {
		return "agregar-editorial.html";
	}
	
	/**
	 * recibe los datos del form para enviarlos al servicio y este se encargue de guardar la editorial
	 * en caso de un error muestra nuevamente la pagina con un mensaje de error
	 * @param modelo
	 * @param nombreEditorial
	 * @return html
	 */
	@PostMapping("/registrar")
	public String guardarEditorial(ModelMap modelo,@RequestParam String nombreEditorial) {
		try {
			editorialServicio.guardarEditorial(nombreEditorial);
			return "redirect:/editoriales";
		} catch (ErrorServicio e) {
			modelo.put("error", e.getMessage());
			return "agregar-editorial.html";
		}
	}
	
	/**
	 * permite dar de baja la editorial que se haya seleccionado(coloca el valor de alta en false)
	 * en caso de error carga nuevamente la pagina con el mismo html y le agrega un mensaje de error
	 * @param modelo
	 * @param id
	 * @return
	 */
	@GetMapping("/baja/{id}")
	public String darBaja(ModelMap modelo,@PathVariable String id) {
		try {
			editorialServicio.baja(id);
			return "redirect:/Editoriales";
		} catch (ErrorServicio e) {
			List<Editorial> editoriales=editorialServicio.buscarTodos();
			modelo.put("editoriales", editoriales);
			modelo.put("error", e.getMessage());
			return "editoriales.html";
		}
	}
	
	/**
	 * permite dar de alta la editorial que se haya seleccionado(coloca el valor de alta en true)
	 * en caso de error carga nuevamente la pagina con el mismo html y le agrega un mensaje de error
	 * @param modelo
	 * @param id
	 * @return
	 */
	@GetMapping("/alta/{id}")
	public String darAlta(ModelMap modelo,@PathVariable String id){
		try {
			editorialServicio.alta(id);
			return "redirect:/editoriales";
		} catch (ErrorServicio e) {
			List<Editorial> editoriales=editorialServicio.buscarTodos();
			modelo.put("editoriales", editoriales);
			modelo.put("error", e.getMessage());
			return "editoriales.html";
		}
	}
	
	/**
	 * carga un form con los datos de la editorial para que estos puedan ser cambiados
	 * @param modelo
	 * @param id
	 * @return
	 */
	@GetMapping("/modificar/{id}")
	public String vistamodificar(ModelMap modelo,@PathVariable String id) {
		try {
			modelo.put("id", id);
			Editorial editorial=editorialServicio.buscarID(id);
			modelo.put("nombreA", editorial.getNombre());
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
	public String modificar(ModelMap modelo,@RequestParam String id,@RequestParam String nombre) {
		try {
			editorialServicio.modificarEditorial(id, nombre);
			return "redirect:/editoriales";
		} catch (ErrorServicio e) {
			modelo.put("error", e.getMessage());
			modelo.put("id", id);
			return "modificarEditorial.html";
		}
	}
}
