package com.aldrin.almacen.dto.reportes;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Rendimiento económico de una sucursal")
public record ReporteVentasSucursalResponse(

        @Schema(description = "Id de la sucrusal", example = "1")
        Long idSucursal,

        @Schema(description = "Nombre de la sucursal")
        String nombreSucursal,

        @Schema(description = "Importe total de la ventas registradas", example = "3500.00")
        BigDecimal totalFacturado,

        @Schema(description = "Unidades vendidas en ventas ya registradas", example = "25")
        Long cantidadProductosVendidos
) {}
