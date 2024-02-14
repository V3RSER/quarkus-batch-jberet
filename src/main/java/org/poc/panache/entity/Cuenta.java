package org.poc.panache.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "cuentas")
public class Cuenta extends PanacheEntityBase {

    @Id
    @Column(name = "numero_cuenta")
    public Integer numeroCuenta;

    public Boolean marcada;

    @Column(name = "numero_registros")
    public Integer numeroRegistros;

    public Double saldo;

    @Column(name = "saldo_total_facturado")
    public Double saldoTotalFacturado;

    @Column(name = "total_tarjetas")
    public Integer totalTarjetas;

    public Cuenta() {
    }
}
