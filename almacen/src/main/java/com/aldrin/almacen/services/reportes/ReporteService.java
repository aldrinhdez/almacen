package com.aldrin.almacen.services.reportes;

import com.aldrin.almacen.dto.reportes.ReporteVentasSucursalResponse;

import java.util.List;

public interface ReporteService {
    List<ReporteVentasSucursalResponse> ventasPorSucursal();
}
