package com.aldrin.almacen.services.ventas;

import com.aldrin.almacen.dto.ventas.VentaRequest;
import com.aldrin.almacen.dto.ventas.VentaResponse;

import java.util.List;

public interface VentaService {

    List<VentaResponse> listar();
    List<VentaResponse> listarCanceladas();
    VentaResponse obtenerPorIdActiva(Long id);
    VentaResponse registrar(VentaRequest request);
    VentaResponse cancelar(Long id);
}
