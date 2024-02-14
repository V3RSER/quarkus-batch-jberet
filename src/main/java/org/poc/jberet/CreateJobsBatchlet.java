package org.poc.jberet;

import io.quarkiverse.jberet.runtime.QuarkusJobOperator;
import io.quarkus.logging.Log;
import jakarta.batch.api.BatchProperty;
import jakarta.batch.api.Batchlet;
import jakarta.batch.api.listener.AbstractJobListener;
import jakarta.batch.runtime.BatchStatus;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.poc.panache.entity.Transaccion;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.*;
import java.util.stream.IntStream;

@Named
@Dependent
public class CreateJobsBatchlet implements Batchlet {

    @Inject
    private QuarkusJobOperator quarkusJobOperator;
    @Inject
    @BatchProperty(name = "batch-size")
    private int batchSize;

    @Override
    public String process() throws Exception {
        long startTime = System.currentTimeMillis();
        createJobs();
        double duration = (System.currentTimeMillis() - startTime) / 1000.0;
        Log.info("Trabajo realizado en " + Math.round(duration * 10.0) / 10.0 + " s.");
        return BatchStatus.COMPLETED.toString();
    }

    @Override
    public void stop() {
        Log.info(this.getClass().getSimpleName() + " stop");
    }

    private void createJobs() {
        int totalPages = calculatePageCount();
        List<Long> jobIds = IntStream.range(0, totalPages)
                .mapToObj(i -> {
                    Properties jobParameters = new Properties();
                    jobParameters.setProperty("page", Integer.toString(i));
                    jobParameters.setProperty("batch-size", Integer.toString(batchSize));
                    return jobParameters;
                })
                .map(jobParameters -> quarkusJobOperator.start("process-job", jobParameters))
                .toList();

        waitForJobs(jobIds);
    }

    private void waitForJobs(List<Long> jobIds) {
        ExecutorService executorService = Executors.newFixedThreadPool(jobIds.size());
        List<Future<Void>> futures = new CopyOnWriteArrayList<>();

        for (Long jobId : jobIds) {
            Future<Void> future = executorService.submit(() -> {
                while (quarkusJobOperator.getJobExecution(jobId).getBatchStatus() != BatchStatus.COMPLETED) {
                    Thread.sleep(150);
                }
                return null;
            });
            futures.add(future);
        }

        for (Future<Void> future : futures) {
            try {
                future.get();
            } catch (ExecutionException ignored) {
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        executorService.shutdown();
    }
    private int calculatePageCount() {
        long totalElementos = Transaccion.getDataCount();
        double resultado = (double) totalElementos / batchSize;
        return (int) Math.ceil(resultado);
    }

    @Named
    @Dependent
    public static class ThreadJobListener extends AbstractJobListener {
        @Inject
        @BatchProperty(name = "page")
        private int page;

        @Inject
        @BatchProperty(name = "batch-size")
        private int batchSize;

        @Override
        public void beforeJob() {
            ThreadCounter.incrementJobCounter(page);
        }

        @Override
        public void afterJob() {
            ThreadCounter.decrementJobCounter(page);
        }
    }

    public static class ThreadCounter {
        private static volatile int runningJobsCounter = 0;
        private static volatile int maxParallelRunningJobCounter = 0;

        public static synchronized void incrementJobCounter(int page) {
            runningJobsCounter++;
            if (runningJobsCounter > maxParallelRunningJobCounter) {
                maxParallelRunningJobCounter = runningJobsCounter;
            }

        }

        public static synchronized void decrementJobCounter(int page) {
//            Log.info("page: " + page + " done.");
            runningJobsCounter--;
        }
    }
}
