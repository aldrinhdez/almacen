package com.aldrin.almacen.dto.ventas;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "DEtalle de una producto dentro de una venta")
public record DetalleVentaResponse(


        @Schema(description = "ID del producto", example = "1")
        Long idProducto,

        @Schema(description = "Nombre del producto", example = "Laptop Gamer")
        String nombreProducto,

        @Schema(description = "Cantidad de unidades vendidas", example = "10")
        Integer cantidadProducto,

        @Schema(description = "Precio unitario al registrar la venta",
                example = "1500.00")
        BigDecimal precioProducto

) {}
