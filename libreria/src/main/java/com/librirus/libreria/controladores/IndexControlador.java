package com.librirus.libreria.controladores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.librirus.libreria.entidades.Autor;
import com.librirus.libreria.entidades.Editorial;
import com.librirus.libreria.entidades.Libro;
import com.librirus.libreria.servicios.AutorServicio;
import com.librirus.libreria.servicios.EditorialServicio;
import com.librirus.libreria.servicios.LibroServicio;

@Controller
@RequestMapping("/")
public class IndexControlador {

	@Autowired
	AutorServicio autorServicio;
	@Autowired
	EditorialServicio editorialServicio;
	@Autowired
	LibroServicio libroServicio;
	
	@GetMapping("/")
	public ModelAndView mostrarHome() {
		return new ModelAndView("index");
	}
	
	@GetMapping("/autores")
	public ModelAndView mostrarAutores() {
		try {
			ModelAndView mav = new ModelAndView("autores");
			List<Autor> autores = autorServicio.buscarTodos();
			mav.addObject("autores", autores);
			return mav;
		} catch (Exception ex) {
			ModelAndView mav = new ModelAndView("autores");
			mav.addObject("error", ex.getMessage());
			return mav;
		}
	}
	
	@GetMapping("/editoriales")
	public ModelAndView mostrarEditoriales() {
		try {
			ModelAndView mav = new ModelAndView("editoriales");
			List<Editorial> editoriales = editorialServicio.buscarTodos();
			mav.addObject("editoriales", editoriales);
			return mav;
		} catch (Exception ex) {
			ModelAndView mav = new ModelAndView("editoriales");
			mav.addObject("error", ex.getMessage());
			return mav;
		}
	}
	
	@GetMapping("/libros")
	public ModelAndView mostrarlibros() {
		try {
			ModelAndView mav = new ModelAndView("libros");
			List<Libro> libros = libroServicio.buscarTodos();
			mav.addObject("libros", libros);
			return mav;
		} catch (Exception ex) {
			ModelAndView mav = new ModelAndView("libros");
			mav.addObject("error", ex.getMessage());
			return mav;
		}
	}
}
