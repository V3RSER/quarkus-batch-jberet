package org.poc.jberet.scheduler;

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
import org.poc.panache.entity.Cuenta;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

@Named
@Dependent
//@Transactional
public class ExecuteJobsBatchlet implements Batchlet {

    @Inject
    private QuarkusJobOperator quarkusJobOperator;
    @Inject
    @BatchProperty(name = "memory-data-limit")
    private int memoryDataLimit;
    @Inject
    @ConfigProperty(name = "quarkus.jberet.max-async")
    private int threads;

    @Override
    public String process() {
        long startTime = System.currentTimeMillis();

        executeJobs();

        double duration = (System.currentTimeMillis() - startTime) / 1000.0;
        Log.info(Cuenta.totalCount() + " items procesados en " + Math.round(duration * 10.0) / 10.0 + " s.");

        return BatchStatus.COMPLETED.toString();
    }

    @Override
    public void stop() {
    }

    private void executeJobs() {
        long totalDataCount = Cuenta.totalCount();
        var pageSize = calculatePageSize(totalDataCount);
        var totalPages = calculateTotalPages(totalDataCount, pageSize);

        List<Long> jobIds = instanceJobs(totalPages, pageSize);
        Log.info("Jobs instanciados: " + jobIds.size());

        waitForJobs(jobIds);
    }

    private int calculatePageSize(long totalDataCount) {
        int pageSize = (int) (totalDataCount > memoryDataLimit ? memoryDataLimit / threads : totalDataCount / threads);
        return pageSize == 0 ? ++pageSize : pageSize;
    }

    private int calculateTotalPages(long totalDataCount, int pageSize) {
        int totalPages = (int) (totalDataCount / pageSize);
        return totalDataCount % pageSize == 0 ? totalPages : ++totalPages;
    }

    private List<Long> instanceJobs(int totalPages, int pageSize) {
        return IntStream.range(0, totalPages)
                .mapToObj(i -> executeJob(i, pageSize))
                .toList();
    }

    private List<Long> instanceJobsAsync(int totalPages, int pageSize) {
        var currentPage = new AtomicInteger(0);
        return IntStream.range(0, totalPages)
                .parallel()
                .mapToObj(i -> executeJob(currentPage.getAndIncrement(), pageSize))
                .toList();
    }

    private Long executeJob(int page, int pageSize) {
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
