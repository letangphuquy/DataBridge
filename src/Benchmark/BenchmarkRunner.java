package Benchmark;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * BenchmarkRunner - Orchestrates a load test benchmark
 * 
 * Usage:
 *   BenchmarkRunner benchmark = new BenchmarkRunner("localhost", 8888);
 *   benchmark.runBenchmark(100, 10, 100);  // 100 clients, 10 messages each, 100ms interval
 * 
 * This demonstrates:
 * - How many concurrent clients the NIO server can handle
 * - Throughput (messages/second)
 * - Latency under load
 * - Memory efficiency compared to thread-per-connection
 */
public class BenchmarkRunner {
    private final String host;
    private final int port;
    private final MetricsCollector metrics;
    
    public BenchmarkRunner(String host, int port) {
        this.host = host;
        this.port = port;
        this.metrics = new MetricsCollector();
    }
    
    /**
     * Run benchmark with specified parameters
     * @param clientCount number of concurrent clients
     * @param messagesPerClient messages to send per client
     * @param messageIntervalMs milliseconds between messages (0 = no delay)
     */
    public void runBenchmark(int clientCount, int messagesPerClient, long messageIntervalMs) {
        System.out.println("========== BENCHMARK CONFIGURATION ==========");
        System.out.println("Server: " + host + ":" + port);
        System.out.println("Clients: " + clientCount);
        System.out.println("Messages per client: " + messagesPerClient);
        System.out.println("Message interval: " + messageIntervalMs + " ms");
        System.out.println("Total expected messages: " + (clientCount * messagesPerClient));
        System.out.println("=============================================\n");
        
        // Create thread pool for clients
        ExecutorService executor = Executors.newFixedThreadPool(Math.min(clientCount, 100));
        
        // Synchronization barrier to start all clients simultaneously
        CountDownLatch startSignal = new CountDownLatch(1);
        
        // Submit all client tasks
        LoadTestClient[] clients = new LoadTestClient[clientCount];
        for (int i = 0; i < clientCount; i++) {
            clients[i] = new LoadTestClient(host, port, i, messagesPerClient, 
                                           messageIntervalMs, metrics, startSignal);
            executor.submit(clients[i]);
        }
        
        System.out.println("Submitted " + clientCount + " clients. Starting benchmark...\n");
        
        // Wait a moment for clients to connect, then signal them to start
        try {
            Thread.sleep(500);
            startSignal.countDown();  // Signal all clients to start
            
            // Wait for all clients to finish
            executor.shutdown();
            boolean completed = executor.awaitTermination(5, TimeUnit.MINUTES);
            
            if (!completed) {
                System.out.println("Benchmark timeout!");
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            System.err.println("Benchmark interrupted");
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
        
        // Collect and print results
        metrics.finish();
        metrics.printSummary();
    }
    
    /**
     * Run a quick benchmark (suitable for quick testing)
     */
    public void runQuickBenchmark() {
        runBenchmark(100, 100, 0);
    }
    
    /**
     * Run a stress test (many clients, fewer messages, longer duration)
     */
    public void runStressTest() {
        runBenchmark(1000, 50, 10);
    }
    
    /**
     * Main entry point for running benchmarks
     */
    public static void main(String[] args) {
        String host = "localhost";
        int port = 8888;
        
        // Parse command line arguments
        if (args.length >= 1) host = args[0];
        if (args.length >= 2) port = Integer.parseInt(args[1]);
        
        BenchmarkRunner runner = new BenchmarkRunner(host, port);
        
        // Run different benchmark scenarios
        System.out.println("DataBridge NioServer Benchmark Suite\n");
        
        // Quick benchmark
        System.out.println("=== Quick Benchmark (100 clients, 100 messages) ===");
        runner = new BenchmarkRunner(host, port);
        runner.runQuickBenchmark();
        
        // Stress test
        System.out.println("\n=== Stress Test (1000 clients, 50 messages) ===");
        runner = new BenchmarkRunner(host, port);
        runner.runStressTest();
        
        System.out.println("\nBenchmark suite completed!");
    }
}
