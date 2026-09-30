package com.aldrin.almacen.controllers;

import com.aldrin.almacen.docs.ProblemaDoc;
import com.aldrin.almacen.dto.productos.ProductoRequest;
import com.aldrin.almacen.dto.productos.ProductoResponse;
import com.aldrin.almacen.dto.sucursales.SucursalRequest;
import com.aldrin.almacen.dto.sucursales.SucursalResponse;
import com.aldrin.almacen.services.sucursales.SucursalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/sucursales")
@RequiredArgsConstructor
public class SucursalController {
    public final SucursalService sucursalService;


    @GetMapping
    @Operation(summary = "Listar Sucrusales", description = "Todos los filtros son opcionales.")
    @ApiResponse(responseCode = "200", description = "Listado obtenido")
    public ResponseEntity<List<SucursalResponse>> listar(
            @Parameter(description = "Búsqueda por nombre", example = "sucursal 1")
            @RequestParam(required = false) String nombre,
            @Parameter(description = "Filtrar por direccion", example = "calle 5")
            @RequestParam(required = false) String direccion
    ) {
        return ResponseEntity.ok(sucursalService.listar(nombre, direccion));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener sucursal por ID")
    @ApiResponse(responseCode = "200", description = "sucursal encontrado")
    @ApiResponse(responseCode = "404", description = "la sucursal no existe",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemaDoc.class)))
    public ResponseEntity<SucursalResponse> obtenerPorId(
            @Parameter(description = "Id de la sucursal", example = "1")
            @PathVariable @Positive(message = "El ID debe ser positivo") Long id
    ) {
        return ResponseEntity.ok(sucursalService.obtenerPorId(id));
    }


    @PostMapping
    @Operation(summary = "Registrar una nueva sucursal")
    @ApiResponse(responseCode = "201", description = "Sucursal creada")
    @ApiResponse(responseCode = "409", description = "Conflicto con datos existentes",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemaDoc.class)))
    public ResponseEntity<SucursalResponse> registrar(@Valid @RequestBody SucursalRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(sucursalService.registrar(request));
    }


    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una sucursal existente")
    @ApiResponse(responseCode = "200", description = "sucursal actualizada")
    @ApiResponse(responseCode = "404", description = "La sucursal no existe",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemaDoc.class)))
    public ResponseEntity<SucursalResponse> actualizar(
            @Parameter(description = "Id del producto", example = "1")
            @PathVariable @Positive(message = "El ID debe ser positivo") Long id,
            @Valid @RequestBody SucursalRequest request
    ) {
        return ResponseEntity.ok(sucursalService.actualizar(request, id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una sucursal")
    @ApiResponse(responseCode = "204", description = "Sucursal eliminada")
    @ApiResponse(responseCode = "404", description = "La sucursal no existe",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemaDoc.class)))
    @ApiResponse(responseCode = "409", description = "La sucursal está en uso y no puede eliminarse",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemaDoc.class)))
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "Id de la sucursal", example = "1")
            @PathVariable @Positive(message = "El ID debe ser positivo") Long id
    ) {
        sucursalService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

}
