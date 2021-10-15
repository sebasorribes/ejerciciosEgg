package com.librirus.libreria.servicios;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.librirus.libreria.entidades.Autor;
import com.librirus.libreria.entidades.Editorial;
import com.librirus.libreria.entidades.Libro;
import com.librirus.libreria.errores.ErrorServicio;
import com.librirus.libreria.repositorios.AutorRepositorio;
import com.librirus.libreria.repositorios.EditorialRespositorio;
import com.librirus.libreria.repositorios.LibroRepositorio;

@Service
public class LibroServicio {

	@Autowired
	LibroRepositorio lr;
	@Autowired
	AutorRepositorio ar;
	@Autowired
	EditorialRespositorio er;
	
	/**
	 * permite guardar un libro en la base de datos
	 * 
	 * @param isbn
	 * @param titulo
	 * @param anio
	 * @param ejemplares
	 * @param ejemplaresprestados
	 * @param autor
	 * @param editorial
	 * @throws ErrorServicio
	 */
	@Transactional
	public void guardarLibro(Long isbn,String titulo,Integer anio,Integer ejemplares,
			Integer ejemplaresprestados,String idAutor,String idEditorial) throws ErrorServicio {
		
		Optional<Autor> resultadoAutor=ar.findById(idAutor);
		Optional<Editorial> resultadoEditorial=er.findById(idEditorial);
		
		
		validar(isbn, titulo, anio, ejemplares, ejemplaresprestados, resultadoAutor, resultadoEditorial);
		Autor autor=resultadoAutor.get();
		Editorial editorial= resultadoEditorial.get();
		
		Libro libro=new Libro();
		
		libro.setIsbn(isbn);
		libro.setTitulo(titulo);
		libro.setAnio(anio);
		libro.setEjemplares(ejemplares);
		libro.setEjemplaresPrestados(ejemplaresprestados);
		libro.setEjemplaresRestantes(ejemplares-ejemplaresprestados);
		libro.setAutor(autor);
		libro.setEditorial(editorial);
		
		lr.save(libro);
	}
	
	/**
	 * devuelve una lista con todos los libros de la base de datos
	 * @return lista de libros
	 */
	@Transactional (readOnly = true)
	public List<Libro> buscarTodos(){
		return lr.findAll();
	}
	
	/**
	 * modifica un libro existente en la base de datos
	 * 
	 * @param id
	 * @param isbn
	 * @param titulo
	 * @param anio
	 * @param ejemplares
	 * @param ejemplaresprestados
	 * @param autor
	 * @param editorial
	 * @throws ErrorServicio
	 */
	@Transactional
	public void modificarLibro(String id,Long isbn,String titulo,Integer anio,Integer ejemplares,
			Integer ejemplaresprestados,String idAutor,String idEditorial) throws ErrorServicio{
		
		Optional<Autor> resultadoAutor=ar.findById(idAutor);
		Optional<Editorial> resultadoEditorial=er.findById(idEditorial);
		
		validar(isbn, titulo, anio, ejemplares, ejemplaresprestados, resultadoAutor, resultadoEditorial);
		
		Autor autor=resultadoAutor.get();
		Editorial editorial= resultadoEditorial.get();
		
		Optional<Libro> respuesta=lr.findById(id);
		if (respuesta.isPresent()) {			
		Libro libro=respuesta.get();
		
		libro.setIsbn(isbn);
		libro.setTitulo(titulo);
		libro.setAnio(anio);
		libro.setEjemplares(ejemplares);
		libro.setEjemplaresPrestados(ejemplaresprestados);
		libro.setEjemplaresRestantes(ejemplares-ejemplaresprestados);
		libro.setAutor(autor);
		libro.setEditorial(editorial);
		
		lr.save(libro);
		}else {
			throw new ErrorServicio("no se encontro el libro seleccionado");
		}
	}
	
	/**
	 * da de baja un libro en la base de datos
	 * @param id
	 */
	@Transactional
	public void baja(String id) throws ErrorServicio{
		Optional<Libro> respuesta=lr.findById(id);
		if (respuesta.isPresent()) {			
		Libro libro=respuesta.get();
		if(libro.getAlta()) {
			libro.setAlta(false);
			
			lr.save(libro);
		}else {
			throw new ErrorServicio("el libro ya esta dado de baja");
		}
		}else {
			throw new ErrorServicio("no se encontro el libro seleccionado");
		}
	}
	
	/**
	 * da de alta un libro que fue dado de baja en la base de datos
	 * 
	 * @param id
	 * @throws ErrorServicio
	 */
	@Transactional
	public void alta(String id) throws ErrorServicio{
		Optional<Libro> respuesta=lr.findById(id);
		if (respuesta.isPresent()) {			
		Libro libro=respuesta.get();
		if(!libro.getAlta()) {
			libro.setAlta(true);
			
			lr.save(libro);
		}else {
			throw new ErrorServicio("el libro ya esta dado de alta");
		}
		}else {
			throw new ErrorServicio("no se encontro el libro seleccionado");
		}
	}
	
	/**
	 * devuelve un libro buscandolo por su id
	 * @param id
	 * @return Libro
	 * @throws ErrorServicio
	 */
	@Transactional
	public Libro buscarId(String id) throws ErrorServicio{
		if(id.isEmpty() || id==null) {
			throw new ErrorServicio("no hay id para buscare ese libro");
		}
		return lr.getById(id);
	}
	
	/**
	 * comprueba que los datos de un libro no sean nulos o tengan un valor indeseado,
	 * en caso que suceda, lanza una excepcion
	 * @param isbn
	 * @param titulo
	 * @param anio
	 * @param ejemplares
	 * @param ejemplaresprestados
	 * @param autor
	 * @param editorial
	 * @throws ErrorServicio
	 */
	@Transactional
	private void validar(Long isbn,String titulo,Integer anio,Integer ejemplares,
			Integer ejemplaresprestados,Optional<Autor> autor,Optional<Editorial> editorial) throws ErrorServicio {
		
		if(isbn==null || isbn==0) {
			throw new ErrorServicio("no puede ingresar un libro sin isbn");
		}
		
		if(titulo==null || titulo.isEmpty()) {
			throw new ErrorServicio("no puede ingresar un libro sin titulo");
		}
		
		if(anio==null || anio==0) {
			throw new ErrorServicio("no puede ingresar un libro nulo o de año 0");
		}
		
		if(ejemplares==null || ejemplares==0) {
			throw new ErrorServicio("no puede ingresar un libro sin ejemplares");
		}
		
		if(ejemplaresprestados==null || ejemplaresprestados>ejemplares) {
			throw new ErrorServicio("no puede ingresar un libro sin un valor de ejemplaresprestados o "
					+ "que tenga mas ejemplares prestados que los totales");
		}
		
		if(!autor.isPresent()) {
			throw new ErrorServicio("no puede ingresar un libro sin autor");
		}
		
		if(!editorial.isPresent()) {
			throw new ErrorServicio("no puede ingresar un libro sin editorial");
		}
	}
	
}
