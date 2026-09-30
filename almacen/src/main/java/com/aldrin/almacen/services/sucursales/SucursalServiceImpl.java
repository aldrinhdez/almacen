package com.aldrin.almacen.services.sucursales;

import com.aldrin.almacen.dto.sucursales.SucursalRequest;
import com.aldrin.almacen.dto.sucursales.SucursalResponse;
import com.aldrin.almacen.entities.Sucursal;
import com.aldrin.almacen.exceptions.ConflictoException;
import com.aldrin.almacen.exceptions.RecursoNoEncontradoException;
import com.aldrin.almacen.mappers.SucursalMapper;
import com.aldrin.almacen.repositories.SucursalRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class SucursalServiceImpl implements SucursalService {

    private final SucursalRepository sucursalRepository;
    private final SucursalMapper sucursalMapper;

    @Override
    @Transactional(readOnly = true)
    public List<SucursalResponse> listar(String nombre, String direccion) {

        log.info("Listando todos los productos");

        return sucursalRepository.findAll().stream()
                .map(sucursalMapper::entidadAResponse).toList();
        //return List.of();
    }

    @Override
    @Transactional(readOnly = true)
    public SucursalResponse obtenerPorId(Long id) {
        return sucursalMapper.entidadAResponse(obtenerSucursal0Exception(id));
        //return null;
    }

    @Override
    public SucursalResponse registrar(SucursalRequest request) {
        log.info("Registrando nuevo sucursal...");

        validarDatosUnicos(request);
        Sucursal sucursal = sucursalMapper.requestAEntidad(
                request
        );

        sucursalRepository.save(sucursal);

        log.info("Nuevo producto {} registrado ", sucursal.getNombre());
        return sucursalMapper.entidadAResponse(sucursal);
       // return null;
    }

    @Override
    public SucursalResponse actualizar(SucursalRequest request, Long id) {

        Sucursal sucursal = obtenerSucursal0Exception(id);
        validarCambiosUnicos(request, id);
        log.info("Actualizando producto con id {} ", id);

        sucursal.actualizar(
                request.nombre(),
                request.direccion());

        sucursalRepository.saveAndFlush(sucursal);

        log.info("Producto con id {} actualizado correctamente ", id);

        return sucursalMapper.entidadAResponse(sucursal);
        //return null;
    }

    @Override
    public void eliminar(Long id) {
        Sucursal sucursal = obtenerSucursal0Exception(id);

        log.info("Eliminando producto con id {} ", id);

        sucursalRepository.delete(sucursal);
        sucursalRepository.flush();

        log.info("Producto con id {} eliminado correctamente ", id);

    }


    private Sucursal obtenerSucursal0Exception(Long id){

        log.info("Buscando sucursal con id {}", id);
        return sucursalRepository.findById(id).orElseThrow(
                () -> new RecursoNoEncontradoException(
                        "Sucursal no encontrada con id: " + id));

    }

    private void validarDatosUnicos(SucursalRequest request){
        log.info("validando nombre unico....");

        if (sucursalRepository.existsByNombreIgnoreCase(request.nombre().trim()))
            throw new ConflictoException(
                    "Ya existe una sucursal con el nombre de: " + request.nombre()
            );
    }

    private void validarCambiosUnicos(SucursalRequest request, Long id){
        log.info("validando cambio en nombre unico....");

        if (sucursalRepository.existsByNombreIgnoreCaseAndIdNot(request.nombre().trim(),id))
            throw new ConflictoException(
                    "Ya existe una sucursal con el nombre de: " + request.nombre()
            );
    }
}
