package com.aldrin.almacen.services.sucursales;

import com.aldrin.almacen.dto.sucursales.SucursalRequest;
import com.aldrin.almacen.dto.sucursales.SucursalResponse;

import java.util.List;

public interface SucursalService {
    List<SucursalResponse> listar(String nombre, String direccion);

    SucursalResponse obtenerPorId(Long id);
    SucursalResponse registrar(SucursalRequest request);
    SucursalResponse actualizar(SucursalRequest request, Long id);

    void eliminar(Long id);
}
