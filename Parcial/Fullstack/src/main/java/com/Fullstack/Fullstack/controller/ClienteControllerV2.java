package com.Fullstack.Fullstack.controller;

import com.Fullstack.Fullstack.model.Cliente;
import com.Fullstack.Fullstack.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.stream.Collectors;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/v2/cliente")
@Tag(name = "Cliente V2", description = "Operaciones relacionadas con clientes (versión 2)")
public class ClienteControllerV2 {
    @Autowired
    private ClienteService clienteService;

    @Operation(summary = "Obtener todos los clientes", description = "Devuelve una lista de todos los clientes con enlaces HATEOAS.")
    @GetMapping
    public List<EntityModel<Cliente>> getAllClientes() {
        return clienteService.getAllClientes().stream()
            .map(cliente -> EntityModel.of(cliente,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ClienteControllerV2.class).getClienteById(cliente.getId_cliente())).withSelfRel(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ClienteControllerV2.class).getAllClientes()).withRel("clientes"),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ClienteControllerV2.class).updateCliente(cliente.getId_cliente(), cliente)).withRel("actualizar"),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ClienteControllerV2.class).deleteCliente(cliente.getId_cliente())).withRel("eliminar")
            ))
            .collect(Collectors.toList());
    }

    @Operation(summary = "Agregar un cliente", description = "Agrega un nuevo cliente y devuelve el cliente creado con enlaces HATEOAS.")
    @PostMapping
    public EntityModel<Cliente> addCliente(@RequestBody Cliente cliente) {
        Cliente saved = clienteService.save(cliente);
        return EntityModel.of(saved,
            WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ClienteControllerV2.class).getClienteById(saved.getId_cliente())).withSelfRel(),
            WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ClienteControllerV2.class).getAllClientes()).withRel("clientes"),
            WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ClienteControllerV2.class).updateCliente(saved.getId_cliente(), saved)).withRel("actualizar"),
            WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ClienteControllerV2.class).deleteCliente(saved.getId_cliente())).withRel("eliminar")
        );
    }

    @Operation(summary = "Obtener cliente por ID", description = "Devuelve un cliente específico por su ID con enlaces HATEOAS.")
    @GetMapping("/{id_cliente}")
    public EntityModel<Cliente> getClienteById(@PathVariable Long id_cliente) {
        Cliente cliente = clienteService.getClienteById(id_cliente);
        return EntityModel.of(cliente,
            WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ClienteControllerV2.class).getClienteById(id_cliente)).withSelfRel(),
            WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ClienteControllerV2.class).getAllClientes()).withRel("clientes"),
            WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ClienteControllerV2.class).updateCliente(id_cliente, cliente)).withRel("actualizar"),
            WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ClienteControllerV2.class).deleteCliente(id_cliente)).withRel("eliminar")
        );
    }

    @Operation(summary = "Eliminar cliente", description = "Elimina un cliente por su ID.")
    @DeleteMapping("/{id_cliente}")
    public ResponseEntity<Void> deleteCliente(@PathVariable Long id_cliente) {
        clienteService.delete(id_cliente);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Actualizar cliente", description = "Actualiza un cliente existente y devuelve el cliente actualizado con enlace HATEOAS.")
    @PutMapping("/{id_cliente}")
    public EntityModel<Cliente> updateCliente(@PathVariable Long id_cliente, @RequestBody Cliente cliente) {
        Cliente updated = clienteService.updateCliente(id_cliente, cliente);
        return EntityModel.of(updated,
            WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ClienteControllerV2.class).getClienteById(id_cliente)).withSelfRel());
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<String> handleNotFound(ResponseStatusException ex) {
        return ResponseEntity.status(ex.getStatusCode()).body(ex.getReason());
    }
}
