package org.poc.panache.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

@Entity
@Cacheable
@Table(name = "cuentas")
public class Cuenta extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cuenta")
    public Integer idCuenta;

    @Column(name = "fecha_registro")
    public String fechaRegistro;

    @Column(name = "desplazamiento_gmt")
    public String desplazamientoGmt;

    @Column(name = "id_cliente")
    public String idCliente;

    @Column(name = "id_transaccion")
    public String idTransaccion;

    @Column(name = "fecha_ledger")
    public String fechaLedger;

    @Column(name = "hora_ledger")
    public String horaLedger;

    @Column(name = "periodo_ledger")
    public String periodoLedger;

    @Column(name = "codigo_moneda")
    public String codigoMoneda;

    @Column(name = "saldo_total")
    public String saldoTotal;

    @Column(name = "saldo_disponible")
    public String saldoDisponible;

    @Column(name = "saldo_no_cobrado")
    public String saldoNoCobrado;

    @Column(name = "saldo_en_espera")
    public String saldoEnEspera;

    @Column(name = "num_entradas_credito")
    public String numEntradasCredito;

    @Column(name = "total_entradas_credito")
    public String totalEntradasCredito;

    @Column(name = "num_entradas_debito")
    public String numEntradasDebito;

    @Column(name = "total_entradas_debito")
    public String totalEntradasDebito;

    @Column(name = "batch_ejecutado")
    public boolean batchEjecutado;


    public Cuenta() {
    }

    public String getFechaRegistro() {
        return fechaRegistro;
    }

    public String getDesplazamientoGmt() {
        return desplazamientoGmt;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public String getIdTransaccion() {
        return idTransaccion;
    }

    public Integer getIdCuenta() {
        return idCuenta;
    }

    public String getFechaLedger() {
        return fechaLedger;
    }

    public String getHoraLedger() {
        return horaLedger;
    }

    public String getPeriodoLedger() {
        return periodoLedger;
    }

    public String getCodigoMoneda() {
        return codigoMoneda;
    }

    public String getSaldoTotal() {
        return saldoTotal;
    }

    public String getSaldoDisponible() {
        return saldoDisponible;
    }

    public String getSaldoNoCobrado() {
        return saldoNoCobrado;
    }


    public String getSaldoEnEspera() {
        return saldoEnEspera;
    }

    public String getNumEntradasCredito() {
        return numEntradasCredito;
    }

    public String getTotalEntradasCredito() {
        return totalEntradasCredito;
    }

    public String getNumEntradasDebito() {
        return numEntradasDebito;
    }

    public String getTotalEntradasDebito() {
        return totalEntradasDebito;
    }
}
