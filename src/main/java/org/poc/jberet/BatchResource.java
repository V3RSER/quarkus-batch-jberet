package org.poc.jberet;

import io.quarkiverse.jberet.runtime.QuarkusJobOperator;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.Serializable;
import java.util.Properties;

@Path("/batch")
@Produces(MediaType.APPLICATION_JSON)
public class BatchResource {

    @Inject
    private QuarkusJobOperator quarkusJobOperator;

    @POST
    @Path("/execute")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response ejecutar(RequestData requestData) {

        Properties jobParameters = new Properties();
        jobParameters.setProperty("batch-size", requestData.getBatchSize());

        long id = quarkusJobOperator.start("scheduler-job", jobParameters);

        return Response.ok(quarkusJobOperator.getJobExecution(id).getBatchStatus().toString()).build();
    }

    public static class RequestData implements Serializable {
        private String batchSize;

        public String getBatchSize() {
            return batchSize;
        }

        public void setBatchSize(String batchSize) {
            this.batchSize = batchSize;
        }
    }
}
