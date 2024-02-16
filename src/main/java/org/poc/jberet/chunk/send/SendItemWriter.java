package org.poc.jberet.chunk.send;

import io.quarkus.logging.Log;
import jakarta.batch.api.BatchProperty;
import jakarta.batch.api.chunk.AbstractItemWriter;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.poc.jberet.DataService;
import org.poc.jberet.dto.CuentaDto;

import java.util.List;

@Dependent
@Named
public class SendItemWriter extends AbstractItemWriter {

    @Inject
    @RestClient
    private DataService dataService;

    @Inject
    @BatchProperty(name = "page")
    private int page;

    @Override
    public void writeItems(List<Object> items) throws InterruptedException {
//        items.forEach(item -> dataService.execute((CuentaDto) item));
        Log.info(" \tPage-" + page + ":\t obtenidos "+ items.size() + " items.");

        for (int i = 0; i < items.size(); i++) {
            CuentaDto item = (CuentaDto) items.get(i);
            try {
                Response response = dataService.execute(item);
                Log.info(" \t --> Page-" + page + ":\t item (" + (i + 1) + "/" + items.size() + ") status: " + response.getStatus());
                response.close();
            } catch (Exception e) {
                Log.error(" \t --> Page-" + page + ":\t item (" + (i + 1) + "/" + items.size() + ") error: " + e.getMessage());
            }
        }

        Log.info(" \t Page-" + page + ":\t enviados " + items.size() + " items.");
    }
}
