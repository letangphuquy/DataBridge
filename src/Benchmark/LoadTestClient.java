package Benchmark;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * LoadTestClient - Simulates a single client connection for load testing
 * 
 * Each client:
 * - Connects to the server
 * - Sends messages at a specified rate
 * - Measures latency and throughput
 * - Handles connection errors gracefully
 */
public class LoadTestClient implements Runnable {
    private final String host;
    private final int port;
    private final int clientId;
    private final int messageCount;
    private final long messageIntervalMs;
    private final MetricsCollector metrics;
    private final CountDownLatch startSignal;
    private final AtomicBoolean errorOccurred;
    
    public LoadTestClient(String host, int port, int clientId, int messageCount, 
                         long messageIntervalMs, MetricsCollector metrics, 
                         CountDownLatch startSignal) {
        this.host = host;
        this.port = port;
        this.clientId = clientId;
        this.messageCount = messageCount;
        this.messageIntervalMs = messageIntervalMs;
        this.metrics = metrics;
        this.startSignal = startSignal;
        this.errorOccurred = new AtomicBoolean(false);
    }
    
    @Override
    public void run() {
        try {
            // Wait for start signal
            startSignal.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }
        
        Socket socket = null;
        try {
            // Connect to server
            socket = new Socket(host, port);
            metrics.incrementConnections();
            System.out.println("Client " + clientId + " connected");
            
            // Create I/O streams
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
            // Send messages
            for (int i = 0; i < messageCount; i++) {
                try {
                    // Send test message
                    long sendTime = System.currentTimeMillis();
                    StringBuilder msgBuilder = new StringBuilder();
                    msgBuilder.append("CLIENT_").append(clientId).append("_MSG_").append(i);
                    String message = msgBuilder.toString();
                    out.write(message);
                    out.newLine();
                    out.flush();
                    
                    // Receive acknowledgment
                    String response = in.readLine();
                    long receiveTime = System.currentTimeMillis();
                    
                    if (response != null) {
                        long latency = receiveTime - sendTime;
                        metrics.recordMessage(latency);
                    } else {
                        metrics.recordError();
                    }
                    
                    // Sleep between messages if interval specified
                    if (messageIntervalMs > 0) {
                        Thread.sleep(messageIntervalMs);
                    }
                } catch (IOException e) {
                    metrics.recordError();
                    System.err.println("Client " + clientId + " I/O error: " + e.getMessage());
                    break;
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
            
            out.close();
            in.close();
            
        } catch (IOException e) {
            metrics.recordError();
            errorOccurred.set(true);
            System.err.println("Client " + clientId + " connection error: " + e.getMessage());
        } finally {
            if (socket != null && !socket.isClosed()) {
                try {
                    socket.close();
                    metrics.decrementConnections();
                } catch (IOException e) {
                    // Ignore
                }
            }
        }
    }
    
    public boolean hadError() {
        return errorOccurred.get();
    }
}
