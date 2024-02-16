package org.poc.jberet.chunk.send;

import jakarta.batch.api.chunk.ItemProcessor;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Named;
import org.poc.jberet.dto.CuentaDto;
import org.poc.panache.entity.Cuenta;

@Dependent
@Named
public class SendItemProcessor implements ItemProcessor {

        @Override
        public Object processItem(Object item) {
                Cuenta entity = (Cuenta) item;

                String numCuenta = entity.getIdCuenta().toString();
                String fechaRegistro = entity.getFechaRegistro();
                String desplazamientoGmt = entity.getDesplazamientoGmt();
                String idCliente = entity.getIdCliente();
                String idTransaccion = entity.getIdTransaccion();
                String fechaLedger = entity.getFechaLedger();
                String horaLedger = entity.getHoraLedger();
                String periodoLedger = entity.getPeriodoLedger();
                String codigoMoneda = entity.getCodigoMoneda();
                String saldoTotal = entity.getSaldoTotal();
                String saldoDisponible = entity.getSaldoDisponible();
                String saldoNoCobrado = entity.getSaldoNoCobrado();
                String saldoEnEspera = entity.getSaldoEnEspera();
                String numEntradasCredito = entity.getNumEntradasCredito();
                String totalEntradasCredito = entity.getTotalEntradasCredito();
                String numEntradasDebito = entity.getNumEntradasDebito();
                String totalEntradasDebito = entity.getTotalEntradasDebito();


                CuentaDto cuentaDto = new CuentaDto();

                cuentaDto.setFechaRegistro(fechaRegistro);
                cuentaDto.setDesplazamientoGmt(desplazamientoGmt);
                cuentaDto.setIdCliente(idCliente);
                cuentaDto.setIdTransaccion(idTransaccion);
                cuentaDto.setNumCuenta(numCuenta);
                cuentaDto.setFechaLedger(fechaLedger);
                cuentaDto.setHoraLedger(horaLedger);
                cuentaDto.setPeriodoLedger(periodoLedger);
                cuentaDto.setCodigoMoneda(codigoMoneda);
                cuentaDto.setSaldoTotal(saldoTotal);
                cuentaDto.setSaldoDisponible(saldoDisponible);
                cuentaDto.setSaldoNoCobrado(saldoNoCobrado);
                cuentaDto.setSaldoEnEspera(saldoEnEspera);
                cuentaDto.setNumEntradasCredito(numEntradasCredito);
                cuentaDto.setTotalEntradasCredito(totalEntradasCredito);
                cuentaDto.setNumEntradasDebito(numEntradasDebito);
                cuentaDto.setTotalEntradasDebito(totalEntradasDebito);

                return cuentaDto;
        }

}
