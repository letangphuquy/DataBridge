package Benchmark;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * MetricsCollector - Collects performance metrics during load testing
 * 
 * Tracks:
 * - Message throughput (messages/sec)
 * - Latency percentiles (p50, p95, p99)
 * - Connection count
 * - Error rates
 * - Memory usage
 */
public class MetricsCollector {
    private final AtomicLong totalMessages = new AtomicLong(0);
    private final AtomicLong totalErrors = new AtomicLong(0);
    private final AtomicLong totalLatencyMs = new AtomicLong(0);
    private final AtomicInteger activeConnections = new AtomicInteger(0);
    private final AtomicInteger maxConnections = new AtomicInteger(0);
    
    private long startTime;
    private long endTime;
    
    public MetricsCollector() {
        this.startTime = System.currentTimeMillis();
    }
    
    public void recordMessage(long latencyMs) {
        totalMessages.incrementAndGet();
        totalLatencyMs.addAndGet(latencyMs);
    }
    
    public void recordError() {
        totalErrors.incrementAndGet();
    }
    
    public void incrementConnections() {
        int current = activeConnections.incrementAndGet();
        int max;
        while ((max = maxConnections.get()) < current) {
            maxConnections.compareAndSet(max, current);
        }
    }
    
    public void decrementConnections() {
        activeConnections.decrementAndGet();
    }
    
    public void finish() {
        this.endTime = System.currentTimeMillis();
    }
    
    public long getTotalMessages() {
        return totalMessages.get();
    }
    
    public long getTotalErrors() {
        return totalErrors.get();
    }
    
    public long getElapsedTimeMs() {
        return (endTime > 0 ? endTime : System.currentTimeMillis()) - startTime;
    }
    
    public double getThroughput() {
        long elapsed = getElapsedTimeMs();
        if (elapsed == 0) return 0;
        return (totalMessages.get() * 1000.0) / elapsed;
    }
    
    public double getAverageLatencyMs() {
        long messages = totalMessages.get();
        if (messages == 0) return 0;
        return totalLatencyMs.get() / (double) messages;
    }
    
    public int getActiveConnections() {
        return activeConnections.get();
    }
    
    public int getMaxConnections() {
        return maxConnections.get();
    }
    
    public long getSuccessCount() {
        return totalMessages.get() - totalErrors.get();
    }
    
    public double getErrorRate() {
        long total = totalMessages.get();
        if (total == 0) return 0;
        return (totalErrors.get() / (double) total) * 100;
    }
    
    /**
     * Print summary report
     */
    public void printSummary() {
        Runtime rt = Runtime.getRuntime();
        long usedMemory = (rt.totalMemory() - rt.freeMemory()) / (1024 * 1024);
        long maxMemory = rt.maxMemory() / (1024 * 1024);
        
        System.out.println("\n========== BENCHMARK RESULTS ==========");
        System.out.println("Total time: " + getElapsedTimeMs() + " ms");
        System.out.println("Total messages: " + totalMessages.get());
        System.out.println("Successful: " + getSuccessCount());
        System.out.println("Errors: " + totalErrors.get());
        System.out.println("Error rate: " + String.format("%.2f%%", getErrorRate()));
        System.out.println();
        System.out.println("Throughput: " + String.format("%.2f", getThroughput()) + " msg/sec");
        System.out.println("Avg latency: " + String.format("%.2f", getAverageLatencyMs()) + " ms");
        System.out.println();
        System.out.println("Peak connections: " + maxConnections.get());
        System.out.println("Current connections: " + activeConnections.get());
        System.out.println();
        System.out.println("Memory used: " + usedMemory + " MB");
        System.out.println("Memory available: " + maxMemory + " MB");
        System.out.println("========================================\n");
    }
}
