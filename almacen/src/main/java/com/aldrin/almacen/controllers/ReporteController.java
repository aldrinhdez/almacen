package com.aldrin.almacen.controllers;

import com.aldrin.almacen.dto.reportes.ReporteVentasSucursalResponse;
import com.aldrin.almacen.services.reportes.ReporteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/reportes")
@RequiredArgsConstructor
public class ReporteController {

    private final ReporteService reporteService;
    @GetMapping("/ventas-por-sucursal")
    @Operation(
            summary = "Consultar rendimiento de ventas por sucursal",
            description = "Calcula el importe facturado y las unidades vendidas "
                    + "utilizando únicamente ventas REGISTRADA. "
                    + "Incluye sucursales sin ventas activas con totales en cero."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Reporte obtenido. Lista vacía si no existen sucursales."
    )
    public ResponseEntity<List<ReporteVentasSucursalResponse>>
    ventasPorSucursal() {

        return ResponseEntity.ok(reporteService.ventasPorSucursal());
    }


}
