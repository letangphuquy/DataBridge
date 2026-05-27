package Server.Core;

import Server.Session.Session;
import Server.Session.SessionManager;
import Server.Session.ConnectionState;
import Rules.HostAddress;
import Server.Database.DatabaseLoader;
import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.net.InetSocketAddress;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.sql.SQLException;

/**
 * NioServer - High-performance, scalable server using Java NIO Selector
 * 
 * Architecture:
 * - Single I/O thread uses Selector for multiplexing thousands of connections
 * - Replaces the thread-per-connection anti-pattern
 * - Can handle 10,000+ concurrent connections without thread explosion
 * - Worker threads handle heavy computation (crypto, DB operations)
 * 
 * Key components:
 * - ServerSocketChannel: Listening socket
 * - Selector: Multiplexes SocketChannels (OP_ACCEPT, OP_READ, OP_WRITE)
 * - ConnectionHandler: Manages individual connection I/O
 * - SessionManager: Thread-safe session storage
 * 
 * Flow:
 * 1. Main thread starts NioServer
 * 2. I/O thread loops in selector.select()
 * 3. For OP_ACCEPT: Create new Session, register with Selector
 * 4. For OP_READ: Read data, parse messages, queue for processing
 * 5. For OP_WRITE: Send queued responses
 * 6. Worker threads process commands asynchronously
 */
public class NioServer {
    private final int port;
    private final SessionManager sessionManager;
    private final ConcurrentHashMap<SocketChannel, ConnectionHandler> handlers;
    
    private ServerSocketChannel serverSocketChannel;
    private Selector selector;
    private volatile boolean running;
    
    private static final long IDLE_TIMEOUT_MS = 30 * 60 * 1000; // 30 minutes
    private static final long CLEANUP_INTERVAL_MS = 60 * 1000;  // 1 minute
    private long lastCleanupTime;
    
    public NioServer(int port) {
        this.port = port;
        this.sessionManager = new SessionManager();
        this.handlers = new ConcurrentHashMap<>();
        this.running = false;
        this.lastCleanupTime = System.currentTimeMillis();
    }
    
    /**
     * Initialize the NIO server
     * Creates ServerSocketChannel and Selector
     * @throws IOException if initialization fails
     */
    public void initialize() throws IOException {
        // Create ServerSocketChannel
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        
        // Bind to port
        InetSocketAddress address = new InetSocketAddress("0.0.0.0", port);
        serverSocketChannel.bind(address);
        
        // Create Selector
        selector = Selector.open();
        
        // Register ServerSocketChannel for ACCEPT events
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        
        System.out.println("NioServer initialized on port " + port);
        System.out.println("Ready to accept connections (multiplexed I/O)");
    }
    
    /**
     * Start the NIO server main loop
     * This runs in the I/O thread and blocks until shutdown
     */
    public void start() {
        running = true;
        System.out.println("NioServer starting I/O loop...");
        
        try {
            // Load database before accepting connections
            try {
                DatabaseLoader.loadAll();
                System.out.println("Database loaded successfully");
            } catch (SQLException e) {
                System.err.println("Error loading database: " + e.getMessage());
                e.printStackTrace();
            }
            
            // Main I/O loop
            while (running) {
                // Periodic cleanup of idle sessions
                long now = System.currentTimeMillis();
                if (now - lastCleanupTime > CLEANUP_INTERVAL_MS) {
                    cleanupIdleSessions();
                    lastCleanupTime = now;
                }
                
                // Blocking select with timeout
                int readyChannels = selector.select(1000); // 1 second timeout
                
                if (readyChannels > 0) {
                    handleSelectedKeys();
                }
            }
        } catch (IOException e) {
            System.err.println("Error in NioServer I/O loop: " + e.getMessage());
            e.printStackTrace();
        } finally {
            shutdown();
        }
    }
    
    /**
     * Process all ready keys from selector
     * @throws IOException if I/O error occurs
     */
    private void handleSelectedKeys() throws IOException {
        Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
        
        while (iterator.hasNext()) {
            SelectionKey key = iterator.next();
            iterator.remove();
            
            try {
                if (key.isAcceptable()) {
                    handleAccept();
                } else if (key.isReadable()) {
                    handleRead(key);
                } else if (key.isWritable()) {
                    handleWrite(key);
                }
            } catch (IOException e) {
                System.err.println("Error handling key: " + e.getMessage());
                // Close the connection on error
                if (key.channel() instanceof SocketChannel) {
                    closeConnection((SocketChannel) key.channel());
                }
            }
        }
    }
    
    /**
     * Handle new client connection (OP_ACCEPT)
     * @throws IOException if accept fails
     */
    private void handleAccept() throws IOException {
        SocketChannel clientChannel = serverSocketChannel.accept();
        if (clientChannel == null) return;
        
        clientChannel.configureBlocking(false);
        
        String remoteAddress = clientChannel.getRemoteAddress().toString();
        System.out.println("New connection from: " + remoteAddress);
        
        // Create session and handler
        Session session = sessionManager.registerSession(clientChannel, remoteAddress);
        ConnectionHandler handler = new ConnectionHandler(session);
        handlers.put(clientChannel, handler);
        
        // Register for read operations
        clientChannel.register(selector, SelectionKey.OP_READ, handler);
        
        System.out.println(sessionManager.getStatsSummary());
    }
    
    /**
     * Handle incoming data (OP_READ)
     * @param key the selection key
     * @throws IOException if read fails
     */
    private void handleRead(SelectionKey key) throws IOException {
        ConnectionHandler handler = (ConnectionHandler) key.attachment();
        
        try {
            // Non-blocking read
            if (!handler.read()) {
                // Connection closed
                closeConnection(handler.getSocketChannel());
                return;
            }
            
            // Extract and process all available messages
            String message;
            while ((message = handler.getNextMessage()) != null) {
                System.out.println("Received from " + handler.getSession().getRemoteAddress() + 
                                 ": " + (message.length() > 100 ? message.substring(0, 100) + "..." : message));
                
                // Queue message for processing by worker threads
                // TODO: Implement message queue and worker thread pool
                // For now, just acknowledge
                handler.sendPlain("ACK");
            }
        } catch (IOException e) {
            System.err.println("Error reading from connection: " + e.getMessage());
            closeConnection(handler.getSocketChannel());
        }
    }
    
    /**
     * Handle outgoing data (OP_WRITE)
     * @param key the selection key
     * @throws IOException if write fails
     */
    private void handleWrite(SelectionKey key) throws IOException {
        // In current implementation, writes happen immediately during read processing
        // In a production system, this would flush a write queue
        key.interestOps(SelectionKey.OP_READ);
    }
    
    /**
     * Close a connection and clean up resources
     */
    private void closeConnection(SocketChannel channel) {
        ConnectionHandler handler = handlers.remove(channel);
        if (handler != null) {
            Session session = handler.getSession();
            System.out.println("Closing connection: " + session);
            
            sessionManager.removeSession(session.getSessionId());
            handler.close();
        }
    }
    
    /**
     * Clean up idle sessions
     */
    private void cleanupIdleSessions() {
        int cleaned = sessionManager.cleanupIdleSessions(IDLE_TIMEOUT_MS);
        if (cleaned > 0) {
            System.out.println("Cleaned up " + cleaned + " idle sessions");
        }
    }
    
    /**
     * Shutdown the server gracefully
     */
    public void shutdown() {
        System.out.println("Shutting down NioServer...");
        running = false;
        
        try {
            // Close all connections
            for (ConnectionHandler handler : handlers.values()) {
                handler.close();
            }
            handlers.clear();
            
            // Close selector and server socket
            if (selector != null && selector.isOpen()) {
                selector.close();
            }
            if (serverSocketChannel != null && serverSocketChannel.isOpen()) {
                serverSocketChannel.close();
            }
            
            System.out.println("NioServer shutdown complete");
        } catch (IOException e) {
            System.err.println("Error during shutdown: " + e.getMessage());
        }
    }
    
    /**
     * Get current session count
     */
    public int getSessionCount() {
        return sessionManager.getActiveSessionCount();
    }
    
    /**
     * Get server statistics
     */
    public String getStats() {
        return sessionManager.getStatsSummary();
    }
    
    // ==================== Main Entry Point ====================
    
    public static void main(String[] args) {
        int port = HostAddress.PORT;
        
        NioServer server = new NioServer(port);
        
        try {
            server.initialize();
            
            // Add shutdown hook for graceful shutdown
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                server.shutdown();
            }));
            
            // Start server (blocks until shutdown)
            server.start();
        } catch (IOException e) {
            System.err.println("Failed to start NioServer: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
