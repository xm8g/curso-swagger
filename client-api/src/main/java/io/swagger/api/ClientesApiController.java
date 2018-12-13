package io.swagger.api;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.api.dao.ClienteDAO;
import io.swagger.model.Cliente;
import io.swagger.model.Clientes;
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2018-12-07T13:30:54.713Z")

@Controller
public class ClientesApiController implements ClientesApi {

    private static final Logger log = LoggerFactory.getLogger(ClientesApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;
    
    @Autowired
    private ClienteDAO dao;

    @org.springframework.beans.factory.annotation.Autowired
    public ClientesApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    /*
    public ResponseEntity<Clientes> consultaTodosClientes() {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<Clientes>(objectMapper.readValue("\"\"", Clientes.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<Clientes>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<Clientes>(HttpStatus.NOT_IMPLEMENTED);
    }*/
    
    
    public ResponseEntity<Clientes> consultaTodosClientes() {
    	ResponseEntity<Clientes> clientes = null;
    	try {
    		List<Cliente> todosClientes = dao.todos();
    		if (todosClientes.isEmpty()) {
    			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    		} else {
    			return new ResponseEntity<>(objectMapper.readValue(objectMapper.writeValueAsString(todosClientes), Clientes.class), HttpStatus.OK);
    		}
    	} catch (Exception e) {
    		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }

}
