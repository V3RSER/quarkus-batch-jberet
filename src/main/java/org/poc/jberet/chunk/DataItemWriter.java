package org.poc.jberet.chunk;

import io.quarkus.logging.Log;
import jakarta.batch.api.chunk.AbstractItemWriter;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Named;
import org.poc.jberet.dto.TransaccionDto;

import java.util.List;

@Dependent
@Named
public class DataItemWriter extends AbstractItemWriter {

    @Override
    public void writeItems(List<Object> items) throws InterruptedException {
//        Log.info("Enviando: " + items.size() + " items.");
        items.forEach(o ->
                {
                    try {
                        var transaccionDto = (TransaccionDto) o;
                        Log.info("\t\tEnviando id-" + transaccionDto.getNumeroTransaccion());
                        Thread.sleep(250);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
        );
    }
}
