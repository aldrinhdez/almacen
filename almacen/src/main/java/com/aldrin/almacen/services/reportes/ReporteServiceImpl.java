package com.aldrin.almacen.services.reportes;

import com.aldrin.almacen.dto.reportes.ReporteVentasSucursalResponse;
import com.aldrin.almacen.enums.EstadoVenta;
import com.aldrin.almacen.repositories.VentaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReporteServiceImpl implements ReporteService {

    private final VentaRepository ventaRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ReporteVentasSucursalResponse> ventasPorSucursal() {

        log.info("Consultando reporte de ventas registradas por sucursal");

        List<ReporteVentasSucursalResponse> reporte =
                ventaRepository.obtenerReportePorSucursal(
                        EstadoVenta.REGISTRADA);

        log.info("Reporte obtenido: {} sucursales", reporte.size());

        return reporte;
    }
}
