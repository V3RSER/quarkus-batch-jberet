package org.poc.jberet;

import io.quarkiverse.jberet.runtime.QuarkusJobOperator;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.poc.jberet.dto.TransaccionDto;

import java.io.Serializable;
import java.util.Properties;

@Path("/batch")
@Produces(MediaType.APPLICATION_JSON)
public class BatchResource {

    @Inject
    private QuarkusJobOperator quarkusJobOperator;

    @Inject
    @ConfigProperty(name = "quarkus.jberet.max-async")
    private int threads;

    @Inject
    @RestClient
    private DataService dataService;

    @POST
    @Path("/execute")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response ejecutar(RequestData requestData) {

        Properties jobParameters = new Properties();
        jobParameters.setProperty("max-batch-in-memory", requestData.getMaxBatchSize());

        long id = quarkusJobOperator.start("scheduler-job", jobParameters);

        return Response.ok(quarkusJobOperator.getJobExecution(id).getBatchStatus().toString()).build();
    }

    public static class RequestData implements Serializable {
        private String maxBatchSize;

        public String getMaxBatchSize() {
            return maxBatchSize;
        }

        public void setMaxBatchSize(String maxBatchSize) {
            this.maxBatchSize = maxBatchSize;
        }
    }
}
