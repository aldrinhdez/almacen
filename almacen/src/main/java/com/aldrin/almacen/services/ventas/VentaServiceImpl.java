package com.aldrin.almacen.services.ventas;

import com.aldrin.almacen.dto.ventas.DetalleVentaRequest;
import com.aldrin.almacen.dto.ventas.VentaRequest;
import com.aldrin.almacen.dto.ventas.VentaResponse;
import com.aldrin.almacen.entities.DetalleVenta;
import com.aldrin.almacen.entities.Producto;
import com.aldrin.almacen.entities.Sucursal;
import com.aldrin.almacen.entities.Venta;
import com.aldrin.almacen.enums.EstadoVenta;
import com.aldrin.almacen.exceptions.ConflictoException;
import com.aldrin.almacen.exceptions.DatoInvalidoException;
import com.aldrin.almacen.exceptions.RecursoNoEncontradoException;
import com.aldrin.almacen.mappers.VentaMapper;
import com.aldrin.almacen.repositories.ProductoRepository;
import com.aldrin.almacen.repositories.SucursalRepository;
import com.aldrin.almacen.repositories.VentaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class VentaServiceImpl implements VentaService {

    private final ProductoRepository productoRepository;
    private final SucursalRepository sucursalRepository;
    private final VentaRepository ventaRepository;
    private final VentaMapper ventaMapper;


    @Override
    @Transactional(readOnly = true)
    public List<VentaResponse> listar() {

        return ventaRepository
                .findByEstadoVenta(EstadoVenta.REGISTRADA)
                .stream()
                .map(ventaMapper::entidadAResponse)
                .toList();
        //return List.of();
    }

    @Override
    @Transactional(readOnly = true)
    public List<VentaResponse> listarCanceladas() {

        return ventaRepository
                .findByEstadoVenta(EstadoVenta.CANCELADA)
                .stream()
                .map(ventaMapper::entidadAResponse)
                .toList();
    }

    @Override
    public VentaResponse obtenerPorIdActiva(Long id) {
        return null;
    }

    @Override
    @Transactional
    public VentaResponse registrar(VentaRequest request) {

        log.info("Iniciando registro de venta");

        if (request == null
                || request.idSucursal() == null
                || request.idSucursal() <= 0) {
            throw new DatoInvalidoException(
                    "La sucursal es requerida y su ID debe ser positivo"
            );
        }

        if (request.productos() == null || request.productos().isEmpty()) {
            throw new DatoInvalidoException(
                    "La venta debe contener al menos un producto"
            );
        }

        log.info("Registrando venta en sucursal {} con {} detalles",
                request.idSucursal(),
                request.productos().size());


        // datis antes de modificar
        for (DetalleVentaRequest detalle : request.productos()) {
            if (detalle == null
                    || detalle.idProducto() == null
                    || detalle.idProducto() <= 0
                    || detalle.cantidadProducto() == null
                    || detalle.cantidadProducto() <= 0) {

                throw new DatoInvalidoException(
                        "Cada detalle debe tener un ID y una cantidad positivos");
            }
        }

        Sucursal sucursal = sucursalRepository
                .findById(request.idSucursal())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Sucursal no encontrada: " + request.idSucursal()));

        Venta venta = Venta.builder()
                .estadoVenta(EstadoVenta.REGISTRADA)
                .fecha(LocalDate.now())
                .sucursal(sucursal)
                .build();

        // Consultar en orden de ID para reducir el riesgo de interbloqueos.
        List<DetalleVentaRequest> detallesOrdenados =
                request.productos().stream()
                        .sorted(Comparator.comparing(
                                DetalleVentaRequest::idProducto))
                        .toList();

        for (DetalleVentaRequest detalleRequest : detallesOrdenados) {

            Producto producto = productoRepository
                    .findById(detalleRequest.idProducto())
                    .orElseThrow(() -> new RecursoNoEncontradoException(
                            "Producto no encontrado: "
                                    + detalleRequest.idProducto()));

            int cantidad = detalleRequest.cantidadProducto();

            if (cantidad > producto.getCantidad()) {
                throw new ConflictoException(
                        "Stock insuficiente para el producto "
                                + producto.getId()
                                + ". Disponible: " + producto.getCantidad()
                                + ", solicitado: " + cantidad);
            }

            BigDecimal precioAlVender = producto.getPrecio();

            producto.descontarCantidad(cantidad);

            DetalleVenta detalle = DetalleVenta.builder()
                    .producto(producto)
                    .cantidadProducto(cantidad)
                    .precioProducto(precioAlVender)
                    .build();

            venta.agregarDetalle(detalle);
        }

        Venta ventaGuardada = ventaRepository.saveAndFlush(venta);

        VentaResponse response = ventaMapper.entidadAResponse(ventaGuardada);

        log.info("Registro de venta {} procesado correctamente",
                ventaGuardada.getId());

        return response;

        //return null;
    }


    @Override
    @Transactional
    public VentaResponse cancelar(Long id) {

        log.info("Iniciando cancelación de venta {}", id);

        if (id == null || id <= 0) {
            throw new DatoInvalidoException(
                    "El id de la venta debe ser positivo"
            );
        }
        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Venta no encontrada: " + id));

        venta.cancelar();

        for (DetalleVenta detalle : venta.getDetalleVentas()) {
            Producto producto = detalle.getProducto();

            producto.aumentarCantidad(
                    detalle.getCantidadProducto());
        }

        ventaRepository.flush();

        VentaResponse response = ventaMapper.entidadAResponse(venta);

        log.info("Cancelación de venta {} procesada correctamente",
                venta.getId());

        return response;
        //return null;

    }
}
