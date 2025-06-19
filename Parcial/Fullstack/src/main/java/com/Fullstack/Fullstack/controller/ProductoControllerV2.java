package com.Fullstack.Fullstack.controller;

import com.Fullstack.Fullstack.model.Producto;
import com.Fullstack.Fullstack.service.ProductoService;
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
@RequestMapping("/api/v2/producto")
@Tag(name = "Producto V2", description = "Operaciones relacionadas con productos (versión 2)")
public class ProductoControllerV2 {
    @Autowired
    private ProductoService productoService;

    @Operation(summary = "Obtener todos los productos", description = "Devuelve una lista de todos los productos con enlaces HATEOAS.")
    @GetMapping
    public List<EntityModel<Producto>> getAllProductos() {
        return productoService.getAllProductos().stream()
            .map(producto -> EntityModel.of(producto,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ProductoControllerV2.class).getProductoById(producto.getId_producto())).withSelfRel()))
            .collect(Collectors.toList());
    }

    @Operation(summary = "Agregar un producto", description = "Agrega un nuevo producto y devuelve el producto creado con enlace HATEOAS.")
    @PostMapping
    public EntityModel<Producto> addProducto(@RequestBody Producto producto) {
        Producto saved = productoService.save(producto);
        return EntityModel.of(saved,
            WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ProductoControllerV2.class).getProductoById(saved.getId_producto())).withSelfRel());
    }

    @Operation(summary = "Obtener producto por ID", description = "Devuelve un producto específico por su ID con enlace HATEOAS.")
    @GetMapping("/{id_producto}")
    public EntityModel<Producto> getProductoById(@PathVariable Long id_producto) {
        Producto producto = productoService.getProductoById(id_producto);
        return EntityModel.of(producto,
            WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ProductoControllerV2.class).getProductoById(id_producto)).withSelfRel());
    }

    @Operation(summary = "Eliminar producto", description = "Elimina un producto por su ID.")
    @DeleteMapping("/{id_producto}")
    public ResponseEntity<Void> deleteProducto(@PathVariable Long id_producto) {
        productoService.delete(id_producto);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Actualizar producto", description = "Actualiza un producto existente y devuelve el producto actualizado con enlace HATEOAS.")
    @PutMapping("/{id_producto}")
    public EntityModel<Producto> updateProducto(@PathVariable Long id_producto, @RequestBody Producto producto) {
        Producto updated = productoService.updateProducto(id_producto, producto);
        return EntityModel.of(updated,
            WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ProductoControllerV2.class).getProductoById(id_producto)).withSelfRel());
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<String> handleNotFound(ResponseStatusException ex) {
        return ResponseEntity.status(ex.getStatusCode()).body(ex.getReason());
    }
}
