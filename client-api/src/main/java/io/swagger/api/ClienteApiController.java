package io.swagger.api;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.ApiParam;
import io.swagger.api.dao.ClienteDAO;
import io.swagger.model.Cliente;
import io.swagger.model.Clientes;
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2018-12-07T13:30:54.713Z")

@Controller
public class ClienteApiController implements ClienteApi {

    private static final Logger log = LoggerFactory.getLogger(ClienteApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;
    
    @Autowired
    private ClienteDAO dao;

    @org.springframework.beans.factory.annotation.Autowired
    public ClienteApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<Cliente> alteraCliente(@ApiParam(value = "",required=true) @PathVariable("id") Integer id,@ApiParam(value = "" ,required=true )  @Valid @RequestBody Cliente cliente) {
    	try {
    		cliente.setId(id);
    		Cliente clienteAlterado = dao.altera(cliente);
	    	if(clienteAlterado == null) {
	    		throw new RuntimeException("Erro ao tentar alterar cliente");
	    	}
	    	return new ResponseEntity<Cliente>(clienteAlterado, getHeaderLocation(id), HttpStatus.ACCEPTED);
	    	
        } catch (Exception e) {
        	log.error("Erro ao alterar cliente", e);
        	return new ResponseEntity<Cliente>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Cliente> alterarStatusById(@ApiParam(value = "",required=true, allowableValues = "\"ativo\", \"inativo\"") @PathVariable("status") String status,@ApiParam(value = "",required=true) @PathVariable("id") Integer id) {
    	
    	try {
    		Cliente clienteAlterado = dao.alteraStatusPorId(id, status);
	    	if(clienteAlterado == null) {
	    		return new ResponseEntity<Cliente>(HttpStatus.NOT_FOUND);
	    	}
	    	return new ResponseEntity<Cliente>(clienteAlterado, getHeaderLocation(id), HttpStatus.ACCEPTED);
	    	
        } catch (Exception e) {
        	log.error("Erro ao alterar cliente", e);
        	return new ResponseEntity<Cliente>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
       

    public ResponseEntity<Void> apagaClientePorId(@ApiParam(value = "",required=true) @PathVariable("id") Integer id) {
        
    	try {
    		boolean excluido = dao.exclui(id);
    		if(excluido) {
    			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    		} else {
    			throw new RuntimeException("Erro ao tentar deletar cliente");
    		}
        } catch (Exception e) {
        	return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    	
    }

    public ResponseEntity<Cliente> cadastraCliente(@ApiParam(value = "" ,required=true )  @Valid @RequestBody Cliente cliente) {
    	try {
    		if (cliente != null) {
	    		Cliente clienteSalvo = dao.salva(cliente);
	    		if(clienteSalvo == null) {
	    			throw new RuntimeException("Erro ao tentar cadastrar novo cliente");
	    		}
	    		
	    		/* Encapsula o id gerado no header */
	    		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(clienteSalvo.getId()).toUri();
	    		MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
	    		headers.add("location", location.getPath());
	    		
	    		return new ResponseEntity<Cliente>(clienteSalvo, headers, HttpStatus.CREATED);
    		} else {
    			return new ResponseEntity<Cliente>(HttpStatus.BAD_REQUEST);
    		}
        } catch (Exception e) {
        	log.error("Erro ao cadastrar cliente", e);
        	return new ResponseEntity<Cliente>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    private MultiValueMap<String, String> getHeaderLocation(Integer id) {
    	URI location = ServletUriComponentsBuilder.fromCurrentRequest().buildAndExpand().toUri();
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
		headers.add("location", location.getPath());
		
		return headers;
    }
    	

    public ResponseEntity<Cliente> consultaClientePorId(@ApiParam(value = "",required=true) @PathVariable("id") Integer id) {
    	try {
    		Cliente clientResult = dao.consultaPorId(id);
	    	if(clientResult == null) {
	    		return new ResponseEntity<Cliente>(HttpStatus.NOT_FOUND);
	    	}
	    	return new ResponseEntity<Cliente>(clientResult, getHeaderLocation(id), HttpStatus.OK);
	    	
        } catch (Exception e) {
        	log.error("Erro ao alterar cliente", e);
        	return new ResponseEntity<Cliente>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Clientes> consultaSobrenome(@ApiParam(value = "",required=true) @PathVariable("sobrenome") String sobrenome) {
    	try {
    		List<Cliente> clientesComMesmoSobrenome = dao.consultaPorSobrenome(sobrenome);
	    	if(clientesComMesmoSobrenome.isEmpty()) {
	    		return new ResponseEntity<Clientes>(HttpStatus.NOT_FOUND);
	    	}
	    	return new ResponseEntity<>(objectMapper.readValue(objectMapper.writeValueAsString(clientesComMesmoSobrenome), Clientes.class), HttpStatus.OK);
	    	
        } catch (Exception e) {
        	log.error("Erro ao alterar cliente", e);
        	return new ResponseEntity<Clientes>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
