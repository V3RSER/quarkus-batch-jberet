package org.poc.jberet.chunk;

import jakarta.batch.api.chunk.AbstractItemWriter;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.poc.jberet.DataService;
import org.poc.jberet.dto.TransaccionDto;

import java.util.List;

@Dependent
@Named
public class DataItemWriter extends AbstractItemWriter {

    @Inject
    @RestClient
    private DataService dataService;

    @Override
    public void writeItems(List<Object> items) throws InterruptedException {
//        Log.info("Enviando: " + items.size() + " items.");
//        Log.info("\t\tEnviando id-" + transaccionDto.getNumeroTransaccion());
        items.forEach(item -> dataService.execute((TransaccionDto) item));
    }
}
