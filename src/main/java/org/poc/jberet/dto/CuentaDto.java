package org.poc.jberet.dto;


import java.io.Serializable;

public class CuentaDto implements Serializable {
    private String fechaRegistro;
    private String desplazamientoGmt;
    private String idCliente;
    private String idTransaccion;
    private String numCuenta;
    private String fechaLedger;
    private String horaLedger;
    private String periodoLedger;
    private String codigoMoneda;
    private String saldoTotal;
    private String saldoDisponible;
    private String saldoNoCobrado;
    private String saldoEnEspera;
    private String numEntradasCredito;
    private String totalEntradasCredito;
    private String numEntradasDebito;
    private String totalEntradasDebito;


    public CuentaDto() {

    }

    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public void setDesplazamientoGmt(String desplazamientoGmt) {
        this.desplazamientoGmt = desplazamientoGmt;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public void setIdTransaccion(String idTransaccion) {
        this.idTransaccion = idTransaccion;
    }

    public void setNumCuenta(String numCuenta) {
        this.numCuenta = numCuenta;
    }

    public void setFechaLedger(String fechaLedger) {
        this.fechaLedger = fechaLedger;
    }

    public void setHoraLedger(String horaLedger) {
        this.horaLedger = horaLedger;
    }

    public void setPeriodoLedger(String periodoLedger) {
        this.periodoLedger = periodoLedger;
    }

    public void setCodigoMoneda(String codigoMoneda) {
        this.codigoMoneda = codigoMoneda;
    }

    public void setSaldoTotal(String saldoTotal) {
        this.saldoTotal = saldoTotal;
    }

    public void setSaldoDisponible(String saldoDisponible) {
        this.saldoDisponible = saldoDisponible;
    }

    public void setSaldoNoCobrado(String saldoNoCobrado) {
        this.saldoNoCobrado = saldoNoCobrado;
    }

    public void setSaldoEnEspera(String saldoEnEspera) {
        this.saldoEnEspera = saldoEnEspera;
    }

    public void setNumEntradasCredito(String numEntradasCredito) {
        this.numEntradasCredito = numEntradasCredito;
    }

    public void setTotalEntradasCredito(String totalEntradasCredito) {
        this.totalEntradasCredito = totalEntradasCredito;
    }

    public void setNumEntradasDebito(String numEntradasDebito) {
        this.numEntradasDebito = numEntradasDebito;
    }

    public void setTotalEntradasDebito(String totalEntradasDebito) {
        this.totalEntradasDebito = totalEntradasDebito;
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

    public String getNumCuenta() {
        return numCuenta;
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

