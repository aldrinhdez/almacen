package com.aldrin.almacen.utils;

import com.aldrin.almacen.entities.Producto;
import com.aldrin.almacen.entities.Sucursal;
import com.aldrin.almacen.enums.Categoria;
import com.aldrin.almacen.repositories.ProductoRepository;
import com.aldrin.almacen.repositories.SucursalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class DatosIniciales implements CommandLineRunner {

    private final ProductoRepository productoRepository;
    private final SucursalRepository sucursalRepository;

    @Override
    public void run(String... args) throws Exception {

        if (productoRepository.count() == 0) {

            productoRepository.saveAll(List.of(

                    new Producto(null,
                            "Laptop Gamer",
                            Categoria.ELECTRONICA,
                            BigDecimal.valueOf(1500),
                            10),

                    new Producto(null,
                            "Mouse Inalámbrico",
                            Categoria.ELECTRONICA,
                            BigDecimal.valueOf(25),
                            50),

                    new Producto(null,
                            "Camiseta Deportiva",
                            Categoria.ROPA,
                            BigDecimal.valueOf(20),
                            100)
            ));
            log.info("Productos de prueba cargados correctamente");
        }

        if(sucursalRepository.count() == 0){
            sucursalRepository.saveAll(List.of(
                    new Sucursal(null,
                            "Sucursal central",
                            "Av. Principal 123")
            ));
            log.info("Productos de prueba cargados correctamente");
        }


    }
}
