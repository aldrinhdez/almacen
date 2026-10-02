package com.aldrin.almacen.repositories;

import com.aldrin.almacen.dto.reportes.ReporteVentasSucursalResponse;
import com.aldrin.almacen.entities.Venta;
import com.aldrin.almacen.enums.EstadoVenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {

    List<Venta> findByEstadoVenta(EstadoVenta estadoVenta);
    @Query("""
    SELECT new com.aldrin.almacen.dto.reportes.ReporteVentasSucursalResponse(
        s.id,
        s.nombre,
        COALESCE(SUM(d.cantidadProducto * d.precioProducto), 0),
        COALESCE(SUM(d.cantidadProducto), 0L)
    )
    FROM Sucursal s
    LEFT JOIN Venta v
        ON v.sucursal = s AND v.estadoVenta = :estado
    LEFT JOIN v.detalleVentas d
    GROUP BY s.id, s.nombre
    ORDER BY s.id
    """)
    List<ReporteVentasSucursalResponse> obtenerReportePorSucursal(
            @Param("estado") EstadoVenta estado);
}
