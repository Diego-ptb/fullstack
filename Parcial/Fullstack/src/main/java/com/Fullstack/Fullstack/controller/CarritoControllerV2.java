package com.Fullstack.Fullstack.controller;

import com.Fullstack.Fullstack.model.Carrito;
import com.Fullstack.Fullstack.service.CarritoService;
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
@RequestMapping("/api/v2/carrito")
@Tag(name = "Carrito V2", description = "Operaciones relacionadas con carritos (versión 2)")
public class CarritoControllerV2 {
    @Autowired
    private CarritoService carritoService;

    @Operation(summary = "Obtener todos los carritos", description = "Devuelve una lista de todos los carritos con enlaces HATEOAS.")
    @GetMapping
    public List<EntityModel<Carrito>> getAllCarritos() {
        return carritoService.getAllCarritos().stream()
            .map(carrito -> EntityModel.of(carrito,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CarritoControllerV2.class).getCarritoById(carrito.getId_carrito())).withSelfRel()))
            .collect(Collectors.toList());
    }

    @Operation(summary = "Agregar un carrito", description = "Agrega un nuevo carrito y devuelve el carrito creado con enlace HATEOAS.")
    @PostMapping
    public EntityModel<Carrito> addCarrito(@RequestBody Carrito carrito) {
        Carrito saved = carritoService.save(carrito);
        return EntityModel.of(saved,
            WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CarritoControllerV2.class).getCarritoById(saved.getId_carrito())).withSelfRel());
    }

    @Operation(summary = "Obtener carrito por ID", description = "Devuelve un carrito específico por su ID con enlace HATEOAS.")
    @GetMapping("/{id_carrito}")
    public EntityModel<Carrito> getCarritoById(@PathVariable Long id_carrito) {
        Carrito carrito = carritoService.getCarritoById(id_carrito);
        return EntityModel.of(carrito,
            WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CarritoControllerV2.class).getCarritoById(id_carrito)).withSelfRel());
    }

    @Operation(summary = "Eliminar carrito", description = "Elimina un carrito por su ID.")
    @DeleteMapping("/{id_carrito}")
    public ResponseEntity<Void> deleteCarrito(@PathVariable Long id_carrito) {
        carritoService.delete(id_carrito);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Actualizar carrito", description = "Actualiza un carrito existente y devuelve el carrito actualizado con enlace HATEOAS.")
    @PutMapping("/{id_carrito}")
    public EntityModel<Carrito> updateCarrito(@PathVariable Long id_carrito, @RequestBody Carrito carrito) {
        Carrito updated = carritoService.updateCarrito(id_carrito, carrito);
        return EntityModel.of(updated,
            WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CarritoControllerV2.class).getCarritoById(id_carrito)).withSelfRel());
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<String> handleNotFound(ResponseStatusException ex) {
        return ResponseEntity.status(ex.getStatusCode()).body(ex.getReason());
    }
}
