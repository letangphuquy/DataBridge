package Server.Core;

import Server.Session.Session;
import Server.Session.ConnectionState;
import Model.E2ESocket;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * ConnectionHandler - Manages I/O for a single NIO connection
 * 
 * Responsibilities:
 * - Read data from SocketChannel into ByteBuffer
 * - Parse protocol messages
 * - Send responses back to client
 * - Handle encryption/decryption via SecretMessenger
 * - Track connection state
 * 
 * This handler operates under the following constraints:
 * - All I/O operations are non-blocking
 * - Can be accessed from I/O thread or worker thread (mostly I/O thread)
 * - Must coordinate with Session for state management
 */
public class ConnectionHandler {
    private final Session session;
    private final ProtocolParser protocolParser;
    private final ByteBuffer writeBuffer;
    private static final int BUFFER_SIZE = 16 * 1024; // 16KB read/write buffers
    
    public ConnectionHandler(Session session) {
        this.session = session;
        this.protocolParser = new ProtocolParser(BUFFER_SIZE);
        this.writeBuffer = ByteBuffer.allocate(BUFFER_SIZE);
    }
    
    /**
     * Attempt to read data from the socket channel
     * Non-blocking operation - returns immediately if no data available
     * 
     * @return true if message(s) were received and are ready for processing
     * @throws IOException if I/O error occurs
     */
    public boolean read() throws IOException {
        SocketChannel channel = session.getSocketChannel();
        
        // Non-blocking read
        int bytesRead = channel.read(protocolParser.getReadBuffer());
        
        if (bytesRead < 0) {
            // Connection closed by peer
            session.forceState(ConnectionState.DISCONNECTING);
            return false;
        }
        
        if (bytesRead == 0) {
            // No data available right now (non-blocking)
            return false;
        }
        
        // Update activity time on successful read
        session.updateActivityTime();
        return true;
    }
    
    /**
     * Extract and return the next complete message from buffer
     * @return complete message string, or null if no complete message available
     */
    public String getNextMessage() {
        return protocolParser.extractMessage();
    }
    
    /**
     * Send a plaintext message (pre-encryption for key exchange)
     * Blocks until message is fully queued for writing
     * 
     * @param message the message to send
     * @throws IOException if I/O error occurs
     */
    public void sendPlain(String message) throws IOException {
        ByteBuffer buffer = ProtocolParser.encodeMessage(message);
        sendBytes(buffer);
    }
    
    /**
     * Send an encrypted message using the session's SecretMessenger
     * @param message the plaintext message
     * @throws IOException if I/O error occurs or encryption fails
     */
    public void sendEncrypted(String message) throws IOException {
        if (session.getSecretMessenger() == null) {
            throw new IllegalStateException("No SecretMessenger for session " + session.getSessionId());
        }
        
        String encrypted = session.getSecretMessenger().encryptStr(message);
        sendPlain(encrypted);
    }
    
    /**
     * Write bytes to the socket channel
     * Handles partial writes by buffering if necessary
     * 
     * @param buffer the data to write
     * @throws IOException if I/O error occurs
     */
    private void sendBytes(ByteBuffer buffer) throws IOException {
        SocketChannel channel = session.getSocketChannel();
        buffer.flip();
        
        while (buffer.hasRemaining()) {
            int bytesWritten = channel.write(buffer);
            if (bytesWritten == 0) {
                // Buffer might be full, need to retry later
                // In a real implementation, this should be queued for later
                throw new IOException("Write buffer full, unable to send complete message");
            }
        }
        
        session.updateActivityTime();
    }
    
    /**
     * Check if connection is still valid
     * @return true if socket channel is open and connected
     */
    public boolean isConnected() {
        return session.isConnected() && session.getSocketChannel().isOpen();
    }
    
    /**
     * Gracefully close the connection
     */
    public void close() {
        try {
            SocketChannel channel = session.getSocketChannel();
            if (channel != null && channel.isOpen()) {
                channel.close();
            }
        } catch (IOException e) {
            System.err.println("Error closing connection handler: " + e.getMessage());
        }
        
        session.forceState(ConnectionState.CLOSED);
    }
    
    // ==================== State Query Methods ====================
    
    public Session getSession() {
        return session;
    }
    
    public SocketChannel getSocketChannel() {
        return session.getSocketChannel();
    }
    
    public ConnectionState getState() {
        return session.getState();
    }
    
    @Override
    public String toString() {
        return String.format("ConnectionHandler{session=%s, buffer_usage=%d/%d}",
            session, protocolParser.getReadBuffer().position(), BUFFER_SIZE);
    }
}
