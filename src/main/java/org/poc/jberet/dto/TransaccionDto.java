package org.poc.jberet.dto;


public class TransaccionDto {

    public Long numeroTransaccion;

    public Integer numeroCuenta;

    public String fecha;

    public Double monto;


    public TransaccionDto() {
    }

    public Long getNumeroTransaccion() {
        return numeroTransaccion;
    }

    public void setNumeroTransaccion(Long numeroTransaccion) {
        this.numeroTransaccion = numeroTransaccion;
    }

    public Integer getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setNumeroCuenta(Integer numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }
}