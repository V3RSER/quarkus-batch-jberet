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
    public Response ejecutar(RequestData requestData) throws InterruptedException {

        Properties jobParameters = new Properties();
        jobParameters.setProperty("memory-data-limit", requestData.getMemoryDataLimit());
        quarkusJobOperator.start("scheduler-job", jobParameters);
        Thread.sleep(800);

        var jobStatus = new BatchStatus();
        var jobsData = quarkusJobOperator.getJobExecutionsByJob("processData").stream()
                .map(jobId -> quarkusJobOperator.getJobExecution(jobId))
                .peek(jobExecution -> jobStatus.evaluate(jobExecution.getBatchStatus()))
                .toList();

        var body = new ResponseData(jobsData.size(), jobStatus);
        return Response.ok(body).build();
    }

    public static class ResponseData implements Serializable {
        public int numJobs;
        public BatchStatus batchStatus;

        public ResponseData(int numJobs, BatchStatus batchStatus) {
            this.numJobs = numJobs;
            this.batchStatus = batchStatus;
        }
    }

    public static class BatchStatus implements Serializable {
        public int STARTING = 0;
        public int STARTED = 0;
        public int STOPPING = 0;
        public int STOPPED = 0;
        public int FAILED = 0;
        public int COMPLETED = 0;
        public int ABANDONED = 0;

        public BatchStatus() {
        }

        public void evaluate(jakarta.batch.runtime.BatchStatus status) {
            switch (status) {
                case STARTING:
                    this.STARTING++;
                    break;
                case STARTED:
                    this.STARTED++;
                    break;
                case STOPPING:
                    this.STOPPING++;
                    break;
                case STOPPED:
                    this.STOPPED++;
                    break;
                case FAILED:
                    this.FAILED++;
                    break;
                case COMPLETED:
                    this.COMPLETED++;
                    break;
                case ABANDONED:
                    this.ABANDONED++;
                    break;
            }
        }
    }

    public static class RequestData implements Serializable {
        private String memoryDataLimit;

        public String getMemoryDataLimit() {
            return memoryDataLimit;
        }

        public void setMemoryDataLimit(String memoryDataLimit) {
            this.memoryDataLimit = memoryDataLimit;
        }
    }
}
