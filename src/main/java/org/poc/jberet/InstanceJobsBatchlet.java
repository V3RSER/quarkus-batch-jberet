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
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.poc.panache.entity.Transaccion;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

@Named
@Dependent
public class InstanceJobsBatchlet implements Batchlet {

    @Inject
    private QuarkusJobOperator quarkusJobOperator;
    @Inject
    @BatchProperty(name = "max-batch-in-memory")
    private int maxBatchSize;
    @Inject
    @ConfigProperty(name = "quarkus.jberet.max-async")
    private int threads;


    @Override
    public String process() {
        long startTime = System.currentTimeMillis();

        instanceJobs();

        double duration = (System.currentTimeMillis() - startTime) / 1000.0;
        Log.info(Transaccion.getDataCount() + " items procesados en " + Math.round(duration * 10.0) / 10.0 + " s.");

        return BatchStatus.COMPLETED.toString();
    }

    @Override
    public void stop() {
    }

    private void instanceJobs() {
        long totalCount = Transaccion.getDataCount();

        int pageSize = calculatePageSize(totalCount);
        int totalPages = calculateTotalPages(totalCount, pageSize);
        AtomicInteger currentPage = new AtomicInteger(0);

        List<Long> jobIds = IntStream.range(0, totalPages)
                .parallel()
                .mapToObj(i -> createJob(currentPage.getAndIncrement(), pageSize))
                .toList();

        Log.info("Jobs instanciados: " + jobIds.size());
        waitForJobs(jobIds);
    }

    private int calculatePageSize(long totalCount) {
        if (totalCount < maxBatchSize) {
            return (int) Math.ceil((double) totalCount / threads);
        }
        return (int) Math.ceil((double) maxBatchSize / threads);
    }

    private int calculateTotalPages(long totalCount, int pageSize) {
        int totalPages = (int) (totalCount / pageSize);
        if (totalCount % pageSize != 0) {
            totalPages++;
        }
        return totalPages;
    }

    private Long createJob(int page, int pageSize) {
        Properties jobParameters = new Properties();
        jobParameters.setProperty("page", Integer.toString(page));
        jobParameters.setProperty("page-size", Integer.toString(pageSize));
        return quarkusJobOperator.start("process-job", jobParameters);
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

    @Named
    @Dependent
    public static class ThreadJobListener extends AbstractJobListener {
        @Inject
        @BatchProperty(name = "page")
        private int page;

        @Inject
        @BatchProperty(name = "page-size")
        private int pageSize;

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
            runningJobsCounter--;
        }
    }

}
