package com.aldrin.almacen.dto.ventas;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DEtalle de una producto dentro de una venta")
public record DetalleVentaResponse(


        @Schema(description = "ID del producto", example = "1")
        Long idProdcuto,

        @Schema(description = "Nombre del prodcuto", example = "Laptop Gamer")
        Long nombreProducto,

        @Schema(description = "Cantidad de unidades vendidas", example = "10")
        Long cantidadProducto,

        @Schema(description = "Precio unitario del prodcuto", example = "1500.00")
        Long precioProducto

) {}
