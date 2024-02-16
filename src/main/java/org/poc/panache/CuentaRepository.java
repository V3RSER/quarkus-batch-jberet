package org.poc.panache;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.poc.panache.entity.Cuenta;

@ApplicationScoped
public interface CuentaRepository extends PanacheRepository<Cuenta> {
    long count();

    PanacheQuery<Cuenta> findAll();
}