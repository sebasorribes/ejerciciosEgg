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
	
	@GetMapping("/agregar-editorial")
	public String formAddEditorial() {
		return "agregar-editorial.html";
	}
	
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
