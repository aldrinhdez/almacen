package com.aldrin.almacen.mappers;

import com.aldrin.almacen.dto.productos.ProductoRequest;
import com.aldrin.almacen.dto.productos.ProductoResponse;
import com.aldrin.almacen.entities.Producto;
import com.aldrin.almacen.enums.Categoria;
import org.springframework.stereotype.Component;

@Component
public class ProductoMapper {

    public Producto requestAEntidad(ProductoRequest request, Categoria categoria) {

        return request == null
                ? null
                : Producto.crear(
                request.nombre(),
                categoria,
                request.precio(),
                request.cantidad());
    }

    public ProductoResponse entidadAResponse(Producto producto) {

        return producto == null
                ? null
                : new ProductoResponse(
                producto.getId(),
                producto.getNombre(),
                producto.getCategoria().getDescripcion(),
                producto.getPrecio(),
                producto.getCantidad());
    }
}
