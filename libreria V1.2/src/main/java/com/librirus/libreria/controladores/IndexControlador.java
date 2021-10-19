package com.librirus.libreria.controladores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.librirus.libreria.entidades.Autor;
import com.librirus.libreria.entidades.Cliente;
import com.librirus.libreria.entidades.Editorial;
import com.librirus.libreria.entidades.Libro;
import com.librirus.libreria.entidades.Prestamo;
import com.librirus.libreria.servicios.AutorServicio;
import com.librirus.libreria.servicios.ClienteServicio;
import com.librirus.libreria.servicios.EditorialServicio;
import com.librirus.libreria.servicios.LibroServicio;
import com.librirus.libreria.servicios.PrestamoServicio;

@Controller
@RequestMapping("/")
public class IndexControlador {

	@Autowired
	AutorServicio autorServicio;
	@Autowired
	EditorialServicio editorialServicio;
	@Autowired
	LibroServicio libroServicio;
	@Autowired
	ClienteServicio clienteServicio;
	@Autowired
	PrestamoServicio prestamoServicio;
	
	/**
	 * devuelve el un modelAndView con el html de index
	 * @return modelAndView con index.html
	 */
	@GetMapping("/")
	public ModelAndView mostrarHome() {
		return new ModelAndView("index");
	}
	
	/**
	 * devuelve el un modelAndView con el html de autores al cual se le cargan todos los autores de la base de datos
	 * para que estos puedan ser visualizados por los usuarios
	 * @return modelAndView con autores.html
	 */
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
	
	/**
	 * devuelve el un modelAndView con el html de editoriales al cual se le cargan todos las editoriales de la base
	 * de datos para que estos puedan ser visualizados por los usuarios
	 * @return modelAndView con editoriales.html
	 */
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
	
	/**
	 * devuelve el un modelAndView con el html de libros al cual se le cargan todos los libros de la base de datos
	 * para que estos puedan ser visualizados por los usuarios
	 * @return modelAndView con libros.html
	 */
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
	
	/**
	 * devuelve el un modelAndView con el html de clientes al cual se le cargan todos los clientes 
	 * de la base de datos para que estos puedan ser visualizados por los usuarios
	 * @return modelAndView con clientes.html
	 */
	@GetMapping("/clientes")
	public ModelAndView mostrarClientes() {
		try {
			ModelAndView mav=new ModelAndView("clientes");
			List<Cliente> clientes=clienteServicio.listaClientesTodo();
			mav.addObject("clientes",clientes);
			return mav;
		}catch(Exception e) {
			ModelAndView mav = new ModelAndView("clientes");
			mav.addObject("error", e.getMessage());
			return mav;
		}
	}
	
	/**
	 * devuelve el un modelAndView con el html de prestamos al cual se le cargan todos los prestamos 
	 * de la base de datos para que estos puedan ser visualizados por los usuarios
	 * @return modelAndView con prestamos.html
	 */
	@GetMapping("/prestamos")
	public ModelAndView mostrarPrestamos() {
		try {
			ModelAndView mav=new ModelAndView("prestamos");
			List<Prestamo> prestamos=prestamoServicio.listaPrestamos();
			mav.addObject("prestamos",prestamos);
			return mav;
		} catch (Exception e) {
			ModelAndView mav=new ModelAndView("prestamos");
			mav.addObject("error", e.getMessage());
			return mav;
		}
	}
}
