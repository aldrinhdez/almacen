package com.aldrin.almacen.mappers;

import com.aldrin.almacen.dto.ventas.DetalleVentaResponse;
import com.aldrin.almacen.dto.ventas.VentaResponse;
import com.aldrin.almacen.entities.Venta;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
public class VentaMapper {

    private final SucursalMapper sucursalMapper;

    public VentaResponse entidadAResponse(Venta venta) {

        if (venta == null) {
            return null;
        }

        List<DetalleVentaResponse> detalles =
                venta.getDetalleVentas().stream()
                        .map(detalle -> new DetalleVentaResponse(
                                detalle.getProducto().getId(),
                                detalle.getProducto().getNombre(),
                                detalle.getCantidadProducto(),
                                detalle.getPrecioProducto()
                        ))
                        .toList();

        BigDecimal total = venta.getDetalleVentas().stream()
                .map(detalle -> detalle.getPrecioProducto()
                        .multiply(BigDecimal.valueOf(
                                detalle.getCantidadProducto())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new VentaResponse(
                venta.getId(),
                venta.getFecha(),
                venta.getEstadoVenta().getCodigo(),
                sucursalMapper.entidadAResponse(venta.getSucursal()),
                detalles,
                total
        );
    }
}
