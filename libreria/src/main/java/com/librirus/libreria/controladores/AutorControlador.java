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

	@GetMapping("/agregar-autor")
	public String formAutor() {
		return "agregar-autor.html";
	}

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
