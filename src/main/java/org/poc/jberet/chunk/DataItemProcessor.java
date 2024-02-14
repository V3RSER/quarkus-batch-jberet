package org.poc.jberet.chunk;

import io.quarkus.logging.Log;
import jakarta.batch.api.chunk.ItemProcessor;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Named;
import org.poc.jberet.dto.TransaccionDto;
import org.poc.panache.entity.Transaccion;

import java.sql.SQLException;

@Dependent
@Named
public class DataItemProcessor implements ItemProcessor {

    @Override
    public Object processItem(Object item) throws SQLException {
        Transaccion transaccionEntity = (Transaccion) item;

        TransaccionDto transaccion = new TransaccionDto();
        transaccion.setNumeroTransaccion(transaccionEntity.getNumeroTransaccion());
        transaccion.setNumeroCuenta(transaccionEntity.getNumeroCuenta());
        transaccion.setFecha(transaccionEntity.getFecha());
        transaccion.setMonto(transaccionEntity.getMonto());

//        Log.info("Procesando transacción id: " + transaccion.getNumeroTransaccion());
        return transaccion;
    }

}
