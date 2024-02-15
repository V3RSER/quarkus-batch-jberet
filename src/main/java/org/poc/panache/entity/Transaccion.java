package org.poc.panache.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;

@Entity
@Cacheable
@Table(name = "transacciones")
@NamedQueries({
        @NamedQuery(
                name = "Transaccion.findByCuentaMarcada",
                query = "SELECT t FROM Transaccion t " +
                        "INNER JOIN Cuenta c ON t.numeroCuenta = c.numeroCuenta " +
                        "WHERE c.marcada = true"
        )
})
public class Transaccion extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "numero_transaccion")
    public Long numeroTransaccion;

    @Column(name = "numero_cuenta")
    public Integer numeroCuenta;

    public String fecha;

    public Double monto;


    public Transaccion() {
    }

    @Transactional
    public static long getDataCount() {
        return Transaccion.count("numeroCuenta in (select c.numeroCuenta from Cuenta c where c.marcada = true)");
    }

    public Long getNumeroTransaccion() {
        return numeroTransaccion;
    }

    public Integer getNumeroCuenta() {
        return numeroCuenta;
    }

    public String getFecha() {
        return fecha;
    }

    public Double getMonto() {
        return monto;
    }

}