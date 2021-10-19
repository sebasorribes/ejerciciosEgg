package com.librirus.libreria.servicios;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.librirus.libreria.entidades.Cliente;
import com.librirus.libreria.errores.ErrorServicio;
import com.librirus.libreria.repositorios.ClienteRepositorio;

@Service
public class ClienteServicio {

	@Autowired
	ClienteRepositorio clienteRepositorio;
	
	/**
	 * guarda un cliente nuevo en la DB
	 * @param dni
	 * @param nombre
	 * @param apellido
	 * @param telefono
	 */
	@Transactional
	public void guardar(Long dni,String nombre,String apellido, String telefono) throws ErrorServicio {
		
		comprobarDatos(dni, nombre, apellido, telefono);
		
		Cliente cliente=new Cliente();
		
		cliente.setDocumento(dni);
		cliente.setNombre(nombre);
		cliente.setApellido(apellido);
		cliente.setTelefono(telefono);
		
		clienteRepositorio.save(cliente);
	}
	
	/**
	 * devuelve todos los clientes en la base de datos, esten dados de alta o baja
	 * @return List<Cliente>
	 */
	@Transactional(readOnly = true)
	public List<Cliente> listaClientesTodo(){
		return clienteRepositorio.findAll();
	}
	
	/**
	 * devuelve una lista con los clientes que tienen el atributo alta en true
	 * @return List<Cliente>
	 */
	@Transactional(readOnly = true)
	public List<Cliente> listaClientesActivos(){
		return clienteRepositorio.clientesActivos();
	}
	
	/**
	 * devuelve una lista de los clientes activos excepto el del parametro
	 * @param cliente
	 * @return List<Cliente>
	 * @throws ErrorServicio
	 */
	public List<Cliente> listaClientesActivosSinSelecto(Cliente cliente){
		List<Cliente> clientes=clienteRepositorio.clientesActivos();
		clientes.remove(cliente);
		
		return clientes;
	}
	
	/**
	 * modifica un cliente que se encuentra en la DB
	 * @param id
	 * @param dni
	 * @param nombre
	 * @param apellido
	 * @param telefono
	 * @throws ErrorServicio
	 */
	@Transactional
	public void modificar(String id,Long dni,String nombre,String apellido, String telefono) throws ErrorServicio {
		comprobarDatos(dni, nombre, apellido, telefono);
		
		Optional<Cliente> resultado=clienteRepositorio.findById(id);
		
		if(resultado.isPresent()) {
			Cliente cliente=resultado.get();
			cliente.setDocumento(dni);
			cliente.setNombre(nombre);
			cliente.setApellido(apellido);
			cliente.setTelefono(telefono);
			
			clienteRepositorio.save(cliente);
		}else {
			throw new ErrorServicio("no se encontro un cliente con ese ID");
		}
	}
	
	/**
	 * da de baja un cliente en la DB
	 * @param id
	 * @throws ErrorServicio
	 */
	@Transactional
	public void baja(String id) throws ErrorServicio {
		Optional<Cliente> resultado=clienteRepositorio.findById(id);
		
		if(resultado.isPresent()) {
			Cliente cliente=resultado.get();
			if(cliente.getAlta()) {
				cliente.setAlta(false);
				clienteRepositorio.save(cliente);
			}else {
				throw new ErrorServicio("el cliente ya se encuentra dado de baja");
			}
		}else {
			throw new ErrorServicio("no se encontro un cliente con ese ID");
		}
	}
	
	/**
	 * da de alta un cliente en la DB
	 * @param id
	 * @throws ErrorServicio
	 */
	@Transactional
	public void alta(String id) throws ErrorServicio{
		Optional<Cliente> resultado=clienteRepositorio.findById(id);
		
		if(resultado.isPresent()) {
			Cliente cliente=resultado.get();
			if(!cliente.getAlta()) {
				cliente.setAlta(true);
				clienteRepositorio.save(cliente);
			}else {
				throw new ErrorServicio("el cliente ya se encuentra dado de alta");
			}
		}else {
			throw new ErrorServicio("no se encontro un cliente con ese ID");
		}
	}
	
	/**
	 * busca por ID y devuelve un cliente 
	 * @param id
	 * @return Cliente
	 * @throws ErrorServicio
	 */
	@Transactional(readOnly = true)
	public Cliente buscarID(String id) throws ErrorServicio {
		Optional<Cliente> resultado=clienteRepositorio.findById(id);
		if(resultado.isPresent()) {
			Cliente cliente=resultado.get();
			return cliente;
		}else {
			throw new ErrorServicio("no hay un cliente con ese ID");
		}
	}
	
	/**
	 * comprueba que reciba todos los datos necesario. En caso de que falte un dato, lanza una excepcion
	 * @param dni
	 * @param nombre
	 * @param apellido
	 * @param telefono
	 */
	public void comprobarDatos(Long dni,String nombre,String apellido, String telefono) throws ErrorServicio {
		if(dni==null || dni.toString().length()>8 || dni.toString().length()<7 || dni<=0) {
			throw new ErrorServicio("el usuario ingresado no tiene un dni valido");
		}
		if(nombre.isEmpty() || nombre==null) {
			throw new ErrorServicio("el usuario ingresado no tiene nombre");
		}
		if(apellido.isEmpty() || apellido==null) {
			throw new ErrorServicio("el usuario ingresado no tiene apellido");
		}
		if(telefono.isEmpty() || telefono==null){
			throw new ErrorServicio("el usuario ingresado no tiene telefono");
		}
	}
}
