package com.aldrin.almacen.dto.ventas;

import com.aldrin.almacen.dto.sucursales.SucursalResponse;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Schema(description = "Datos de una venta")
public record VentaResponse(


        @Schema(description = "Identificador de la venta", example = "1")
        Long id,

        @Schema(description = "Fecha de la venta", example = "2026-10-01")
        LocalDate fecha,

        @Schema(description = "Código del estado: 1 registrada, 0 cancelada",
                example = "1")
        Long estado,

        @Schema(description = "Sucursal donde se realizó la venta")
        SucursalResponse sucursal,

        @Schema(description = "Lista de productos de la venta")
        List<DetalleVentaResponse> detalles,

        @Schema(description = "Total de la venta", example = "15000.00")
        BigDecimal total
) {}
