package org.poc.jberet.chunk;

import jakarta.batch.api.chunk.ItemProcessor;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Named;
import org.poc.jberet.dto.TransaccionDto;
import org.poc.panache.entity.Transaccion;

@Dependent
@Named
public class DataItemProcessor implements ItemProcessor {

    @Override
    public Object processItem(Object item) {
        Transaccion entity = (Transaccion) item;

        return TransaccionDto.aTransaccionDto()
                .withNumeroTransaccion(entity.getNumeroTransaccion())
                .withNumeroCuenta(entity.getNumeroCuenta())
                .withFecha(entity.getFecha())
                .withMonto(entity.getMonto());
    }

}
