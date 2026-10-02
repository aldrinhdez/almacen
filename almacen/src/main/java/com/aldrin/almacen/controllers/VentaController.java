package com.aldrin.almacen.controllers;

import com.aldrin.almacen.docs.ProblemaDoc;
import com.aldrin.almacen.dto.ventas.VentaRequest;
import com.aldrin.almacen.dto.ventas.VentaResponse;
import com.aldrin.almacen.services.ventas.VentaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ventas")
@RequiredArgsConstructor
public class VentaController {

    public final VentaService ventaService;

    @PostMapping
    @Operation(
            summary = "Registrar una venta",
            description = "Registra una venta utilizando el precio actual "
                    + "de cada producto y descuenta las unidades del inventario. "
                    + "Si algún producto no tiene existencias suficientes, "
                    + "se revierte toda la operación."
    )
    @ApiResponse(responseCode = "201", description = "Venta creada")
    @ApiResponse(responseCode = "400", description = "Datos de la venta inválidos",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemaDoc.class)))
    @ApiResponse(responseCode = "404", description = "La sucursal o algún producto no existe",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemaDoc.class)))
    @ApiResponse(responseCode = "409", description = "Existencias insuficientes para algún producto",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemaDoc.class)))
    public ResponseEntity<VentaResponse> registrar(
            @Valid@RequestBody
            VentaRequest request){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ventaService.registrar(request));
    }

    @GetMapping
    @Operation(
            summary = "Listar ventas registradas",
            description = "Devuelve únicamente las ventas en estado REGISTRADA."
    )
    @ApiResponse(responseCode = "200",
            description = "Listado obtenido. Devuelve una lista vacía si no hay ventas registradas.")
    public ResponseEntity<List<VentaResponse>> listar() {

        return ResponseEntity.ok(ventaService.listar());
    }

    @GetMapping("/canceladas")
    @Operation(summary = "Listar ventas canceladas", description = "Consulta el histórico de ventas en estado CANCELADA.")
    @ApiResponse(responseCode = "200", description = "Listado obtenido. Devuelve una lista vacía si no hay ventas canceladas.")
    public ResponseEntity<List<VentaResponse>> listarCanceladas() {

        return ResponseEntity.ok(ventaService.listarCanceladas());
    }

    @PatchMapping("/{id}/cancelar")
    @Operation(
            summary = "Cancelar una venta",
            description = "Cambia el estado de la venta a CANCELADA y devuelve "
                    + "las unidades vendidas al inventario. "
                    + "No permite cancelar una venta que ya esté cancelada."
    )
    @ApiResponse(responseCode = "200", description = "Venta cancelada e inventario actualizado")
    @ApiResponse(responseCode = "400", description = "El ID debe ser positivo")
    @ApiResponse(responseCode = "404", description = "La venta no existe")
    @ApiResponse(responseCode = "409", description = "La venta ya está cancelada")
    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    public ResponseEntity<VentaResponse> cancelar(
            @Parameter(description = "ID de la venta que se desea cancelar",
                    example = "1")
            @PathVariable Long id) {

        return ResponseEntity.ok(ventaService.cancelar(id));
    }

}
