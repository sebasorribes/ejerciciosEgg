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

import com.librirus.libreria.entidades.Cliente;
import com.librirus.libreria.errores.ErrorServicio;
import com.librirus.libreria.servicios.ClienteServicio;

@Controller
@RequestMapping("/clientes")
public class ClienteControlador {

	@Autowired
	ClienteServicio clienteServicio;
	
	/**
	 * muestra el form para llenar los datos del cliente
	 * @return
	 */
	@GetMapping("/registro")
	public String formularioCliente() {
			return "agregar-clientes.html";
	}
	
	/**
	 * recibe los datos del form para enviarlos al servicio y este se encargue de guardar el cliente
	 * en caso de un error muestra nuevamente la pagina con un mensaje de error
	 * 
	 * @param modelo
	 * @param dni
	 * @param nombre
	 * @param apellido
	 * @param telefono
	 * @return
	 */
	@PostMapping("/registro")
	public String registroCliente(ModelMap modelo,@RequestParam(required = false) Long dni,
			@RequestParam(required = false) String nombre,@RequestParam(required = false) String apellido,
			@RequestParam(required = false) String telefono) {
		try {
			clienteServicio.guardar(dni, nombre, apellido, telefono);
			return "redirect:/clientes";
		} catch (ErrorServicio es) {
			modelo.put("error", es.getMessage());
			modelo.addAttribute("nombre", nombre);
			modelo.addAttribute("apellido", apellido);
			modelo.addAttribute("dni", dni);
			modelo.addAttribute("telefono", telefono);
			return "agregar-clientes.html";
		}catch(Exception e) {
			modelo.put("error", e.getMessage());
			modelo.put("nombre", nombre);
			modelo.put("apellido", apellido);
			modelo.put("dni", dni);
			modelo.put("telefono", telefono);
			return "agregar-clientes.html";
		}
	}
	
	/**
	 * permite dar de baja el cliente que se haya seleccionado(coloca el valor de alta en false)
	 * en caso de error carga nuevamente la pagina con el mismo html y le agrega un mensaje de error
	 * @param modelo
	 * @param id
	 * @return
	 */
	@GetMapping("/baja/{id}")
	public String darBaja(ModelMap modelo, @PathVariable String id) {
		try {
			clienteServicio.baja(id);
			return "redirect:/clientes";
		} catch (ErrorServicio e) {
			modelo.put("error", e.getMessage());
			List<Cliente> clientes=clienteServicio.listaClientesTodo();
			modelo.put("clientes", clientes);
			return "clientes.html";
		}
	}
	
	/**
	 * permite dar de alta el cliente que se haya seleccionado(coloca el valor de alta en true)
	 * en caso de error carga nuevamente la pagina con el mismo html y le agrega un mensaje de error
	 * @param modelo
	 * @param id
	 * @return
	 */
	@GetMapping("/alta/{id}")
	public String darAlta(ModelMap modelo, @PathVariable String id) {
		try {
			clienteServicio.alta(id);
			return "redirect:/clientes";
		} catch (ErrorServicio e) {
			modelo.put("error", e.getMessage());
			List<Cliente> clientes=clienteServicio.listaClientesTodo();
			modelo.put("clientes", clientes);
			return "clientes.html";
		}
	}
	
	/**
	 * carga un form con los datos del cliente para que estos puedan ser cambiados
	 * @param modelo
	 * @param id
	 * @return
	 */
	@GetMapping("/modificar/{id}")
	public String formModifCliente(ModelMap modelo, @PathVariable String id) {
		try {
			modelo.put("id", id);
			Cliente cliente=clienteServicio.buscarID(id);
			modelo.put("nombre", cliente.getNombre());
			modelo.put("dni", cliente.getDocumento());
			modelo.put("apellido", cliente.getApellido());
			modelo.put("telefono", cliente.getTelefono());
			return "modificarClientes.html";
		} catch (ErrorServicio e) {
			return "redirect:/clientes";
		}
	}
	
	/**
	 * utiliza el servicio con los datos modificados para cambiarlos en la base de datos
	 * @param modelo
	 * @param id
	 * @param dni
	 * @param nombre
	 * @param apellido
	 * @param telefono
	 * @return
	 */
	@PostMapping("/modificar/{id}")
	public String modificarCliente(ModelMap modelo,@RequestParam(required = false) String id,@RequestParam(required = false) Long dni,
			@RequestParam(required = false) String nombre,@RequestParam(required = false) String apellido,
			@RequestParam(required = false) String telefono) {
		try {
			clienteServicio.modificar(id, dni, nombre, apellido, telefono);
			return "redirect:/clientes";
		} catch (ErrorServicio es) {
			modelo.put("error", es.getMessage());
			modelo.addAttribute("id", id);
			modelo.addAttribute("nombre", nombre);
			modelo.addAttribute("apellido", apellido);
			modelo.addAttribute("dni", dni);
			modelo.addAttribute("telefono", telefono);
			return "modificarClientes.html";
		}catch(Exception e) {
			modelo.put("error", e.getMessage());
			modelo.addAttribute("id", id);
			modelo.put("nombre", nombre);
			modelo.put("apellido", apellido);
			modelo.put("dni", dni);
			modelo.put("telefono", telefono);
			return "modificarClientes.html";
		}
	}
}
