package org.poc.panache.dto;


import java.io.Serializable;

public class TransaccionDto implements Serializable {

    private Long numeroTransaccion;

    private Integer numeroCuenta;

    private String fecha;

    private Double monto;


    public TransaccionDto() {
    }

    public static TransaccionDto aTransaccionDto() {
        return new TransaccionDto();
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

    public void setNumeroTransaccion(Long numeroTransaccion) {
        this.numeroTransaccion = numeroTransaccion;
    }

    public void setNumeroCuenta(Integer numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public TransaccionDto withNumeroTransaccion(Long numeroTransaccion) {
        this.numeroTransaccion = numeroTransaccion;
        return this;
    }

    public TransaccionDto withNumeroCuenta(Integer numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
        return this;
    }

    public TransaccionDto withFecha(String fecha) {
        this.fecha = fecha;
        return this;
    }

    public TransaccionDto withMonto(Double monto) {
        this.monto = monto;
        return this;
    }

}