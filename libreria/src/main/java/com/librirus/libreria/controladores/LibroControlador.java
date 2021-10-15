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
import com.librirus.libreria.entidades.Libro;
import com.librirus.libreria.errores.ErrorServicio;
import com.librirus.libreria.servicios.AutorServicio;
import com.librirus.libreria.servicios.EditorialServicio;
import com.librirus.libreria.servicios.LibroServicio;

@Controller
@RequestMapping("/libros")
public class LibroControlador {

	@Autowired
	LibroServicio libroServicio;
	@Autowired
	AutorServicio autorServicio;
	@Autowired
	EditorialServicio editorialServicio;

	/**
	 * devuelve el html "agregar-libro" que contiene un form para colocar los datos del libro
	 * @param modelo
	 * @return
	 */
	@GetMapping("/agregar-libro")
	public String vistaAgregar(ModelMap modelo) {
		List<Autor> autores = autorServicio.buscarTodos();
		List<Editorial> editoriales = editorialServicio.buscarTodos();
		modelo.put("autores", autores);
		modelo.put("editoriales", editoriales);
		return "agregar-libro.html";
	}

	/**
	 * recibe los datos del form para enviarlos al servicio y este se encargue de guardar el libro,
	 * en caso de un error muestra nuevamente la pagina con un mensaje de error
	 * @param modelo
	 * @param isbn
	 * @param nombreLibro
	 * @param anio
	 * @param ejemplares
	 * @param ejemplaresPrestados
	 * @param autor
	 * @param editorial
	 * @return
	 */
	@PostMapping("/registrar")
	public String agregar(ModelMap modelo, @RequestParam(required = false) Long isbn, @RequestParam String nombreLibro,
			@RequestParam(required = false) Integer anio, @RequestParam(required = false) Integer ejemplares,
			@RequestParam(required = false) Integer ejemplaresPrestados, @RequestParam String autor,
			@RequestParam String editorial) {
		try {
			libroServicio.guardarLibro(isbn, nombreLibro, anio, ejemplares, ejemplaresPrestados, autor, editorial);
			return "redirect:/libros";

		} catch (ErrorServicio e) {
			// TODO: mostrar datos anteriores al haber un error
			modelo.put("error", e.getMessage());
			modelo.put("isbn", isbn);
			modelo.put("nombreLibro", nombreLibro);
			modelo.put("anio", anio);
			modelo.put("ejemplares", ejemplares);
			modelo.put("ejemplaresPrestados", ejemplaresPrestados);
			List<Autor> autores = autorServicio.buscarTodos();
			List<Editorial> editoriales = editorialServicio.buscarTodos();
			modelo.put("autores", autores);
			modelo.put("editoriales", editoriales);
			modelo.put("autor", autor);
			modelo.put("editorial", editorial);
			return "agregar-libro.html";
		}
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
			libroServicio.baja(id);
			return "redirect:/libros";
		} catch (ErrorServicio e) {
			modelo.put("error", e.getMessage());
			List<Libro> libros = libroServicio.buscarTodos();
			modelo.put("libros", libros);
			return "libros.html";
		}
	}

	/**
	 * permite dar de alta el libro que se haya seleccionado(coloca el valor de alta en true)
	 * en caso de error carga nuevamente la pagina con el mismo html y le agrega un mensaje de error
	 * @param modelo
	 * @param id
	 * @return
	 */
	@GetMapping("/alta/{id}")
	public String darAlta(ModelMap modelo, @PathVariable String id) {
		try {
			libroServicio.alta(id);
			return "redirect:/libros";
		} catch (ErrorServicio e) {
			modelo.put("error", e.getMessage());
			return "libros.html";
		}
	}

	/**
	 * carga un form con los datos del libro para que estos puedan ser cambiados
	 * @param modelo
	 * @param id
	 * @return
	 */
	@GetMapping("/modificar/{id}")
	public String modificaLibro(ModelMap modelo, @PathVariable String id) {
		try {
			Libro libro = libroServicio.buscarId(id);
			modelo.put("isbn", libro.getIsbn());
			modelo.put("nombreLibro", libro.getTitulo());
			modelo.put("anio", libro.getAnio());
			modelo.put("ejemplares", libro.getEjemplares());
			modelo.put("ejemplaresPrestados", libro.getEjemplaresPrestados());
			List<Autor> autores = autorServicio.buscarTodos();
			autores.remove(libro.getAutor());
			List<Editorial> editoriales = editorialServicio.buscarTodos();
			editoriales.remove(libro.getEditorial());
			modelo.put("autores", autores);
			modelo.put("editoriales", editoriales);
			modelo.put("autorS", libro.getAutor());
			modelo.put("editorialS", libro.getEditorial());
			modelo.put("id", id);
			return "modificarLibro.html";
		} catch (ErrorServicio e) {
			modelo.put("error", e.getMessage());
			return "/";
		}
	}

	/**
	 * utiliza el servicio con los datos modificados para cambiarlos en la base de datos
	 * @param modelo
	 * @param id
	 * @param isbn
	 * @param nombreLibro
	 * @param anio
	 * @param ejemplares
	 * @param ejemplaresPrestados
	 * @param autor
	 * @param editorial
	 * @return
	 */
	@PostMapping("/modificar/{id}")
	public String modificarHecho(ModelMap modelo, @RequestParam String id, @RequestParam(required = false) Long isbn,
			@RequestParam String nombreLibro, @RequestParam(required = false) Integer anio,
			@RequestParam(required = false) Integer ejemplares,
			@RequestParam(required = false) Integer ejemplaresPrestados, @RequestParam String autor,
			@RequestParam String editorial) {
		try {
			libroServicio.modificarLibro(id, isbn, nombreLibro, anio, ejemplares, ejemplaresPrestados, autor,
					editorial);
			return "redirect:/libros";
		} catch (ErrorServicio e) {
			try {
				modelo.put("error", e.getMessage());
				modelo.put("id", id);
				modelo.put("isbn", isbn);
				modelo.put("nombreLibro", nombreLibro);
				modelo.put("anio", anio);
				modelo.put("ejemplares", ejemplares);
				modelo.put("ejemplaresPrestados", ejemplaresPrestados);
				List<Autor> autores = autorServicio.buscarTodos();
				Autor autorS = autorServicio.buscarId(autor);
				autores.remove(autorS);
				List<Editorial> editoriales = editorialServicio.buscarTodos();
				Editorial editorialS = editorialServicio.buscarID(editorial);
				editoriales.remove(editorialS);
				modelo.put("autores", autores);
				modelo.put("editoriales", editoriales);
				modelo.put("autorS", autorS);
				modelo.put("editorialS", editorialS);
				return "modificarLibro.html";
			} catch (ErrorServicio ex) {
				return "redirect:/libros";
			}

		}
	}
}
