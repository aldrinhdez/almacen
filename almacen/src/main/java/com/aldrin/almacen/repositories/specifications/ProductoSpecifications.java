package com.aldrin.almacen.repositories.specifications;

import com.aldrin.almacen.entities.Producto;
import com.aldrin.almacen.enums.Categoria;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public final class ProductoSpecifications {

    private ProductoSpecifications() {
    }

    public static Specification<Producto> conFiltros(
            String nombre,
            Categoria categoria,
            BigDecimal precioMin,
            BigDecimal precioMax) {

        return (root, query, cb) -> {
            List<Predicate> condiciones = new ArrayList<>();

            if (nombre != null && !nombre.isBlank()) {
                String texto = nombre.trim()
                        .toLowerCase(Locale.ROOT);

                // Converitr cartares para busqyeda en el like
                String patron = "%" + escaparLike(texto) + "%";

                condiciones.add(
                        cb.like(
                                cb.lower(root.<String>get("nombre")),
                                patron,
                                '\\'
                        )
                );
            }

            if (categoria != null) {
                condiciones.add(
                        cb.equal(root.get("categoria"), categoria)
                );
            }

            if (precioMin != null) {
                condiciones.add(
                        cb.greaterThanOrEqualTo(
                                root.<BigDecimal>get("precio"),
                                precioMin
                        )
                );
            }

            if (precioMax != null) {
                condiciones.add(
                        cb.lessThanOrEqualTo(
                                root.<BigDecimal>get("precio"),
                                precioMax
                        )
                );
            }

            return cb.and(condiciones.toArray(new Predicate[0]));
        };
    }

    private static String escaparLike(String texto) {
        return texto.replace("\\", "\\\\")
                .replace("%", "\\%")
                .replace("_", "\\_");
    }
}