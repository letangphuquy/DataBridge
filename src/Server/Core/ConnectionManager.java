package Server.Core;

import Server.Session.Session;
import Server.Session.ConnectionState;
import Model.E2ESocket;
import Model.SecretMessenger;
import java.io.IOException;
import java.nio.channels.SocketChannel;

/**
 * ConnectionManager - Manages the lifecycle and operations of a connection
 * 
 * Responsibilities:
 * - Coordinate key exchange during handshake
 * - Transition connection through states
 * - Bridge between NIO layer and application logic
 * - Handle encryption/decryption coordination
 * 
 * This class acts as a coordinator between the low-level NIO operations
 * (handled by ConnectionHandler) and the application logic.
 */
public class ConnectionManager {
    private final Session session;
    private final ConnectionHandler handler;
    
    public ConnectionManager(Session session, ConnectionHandler handler) {
        this.session = session;
        this.handler = handler;
    }
    
    /**
     * Perform key exchange with client
     * Transition from HANDSHAKING to AUTHENTICATED state
     * 
     * Protocol:
     * 1. Server creates SecretMessenger and sends public key
     * 2. Client responds with public key
     * 3. Server generates shared secret from client's public key
     * 4. Both parties can now encrypt/decrypt messages
     * 
     * @throws IOException if communication fails
     */
    public void performKeyExchange() throws IOException {
        if (session.getState() != ConnectionState.HANDSHAKING) {
            throw new IllegalStateException("Cannot perform key exchange from state: " + session.getState());
        }
        
        try {
            // Create secret messenger for this session
            SecretMessenger secretMessenger = new SecretMessenger();
            session.setSecretMessenger(secretMessenger);
            
            // Send server's public key to client
            byte[] serverPublicKey = secretMessenger.getPublicKey();
            String keyMessage = "KEY " + bytesToHexString(serverPublicKey);
            handler.sendPlain(keyMessage);
            System.out.println("Sent server public key to " + session.getRemoteAddress());
            
            // TODO: Implement async read for client's public key
            // For now, this should be handled by the main NIO loop
            // When client's KEY message arrives, call completeKeyExchange()
            
        } catch (Exception e) {
            System.err.println("Error during key exchange: " + e.getMessage());
            e.printStackTrace();
            session.transitionTo(ConnectionState.DISCONNECTING);
            throw new IOException("Key exchange failed", e);
        }
    }
    
    /**
     * Complete key exchange after receiving client's public key
     * @param clientPublicKeyHex the client's public key in hex format
     * @throws IOException if key agreement fails
     */
    public void completeKeyExchange(String clientPublicKeyHex) throws IOException {
        SecretMessenger secretMessenger = session.getSecretMessenger();
        if (secretMessenger == null) {
            throw new IllegalStateException("No SecretMessenger initialized");
        }
        
        try {
            byte[] clientPublicKey = hexStringToBytes(clientPublicKeyHex);
            secretMessenger.generateSharedSecret(clientPublicKey);
            
            System.out.println("Key exchange completed for " + session.getRemoteAddress());
            
            // Transition to AUTHENTICATED state
            if (!session.transitionTo(ConnectionState.AUTHENTICATED)) {
                throw new IllegalStateException("Failed to transition to AUTHENTICATED state");
            }
        } catch (Exception e) {
            System.err.println("Error completing key exchange: " + e.getMessage());
            e.printStackTrace();
            session.transitionTo(ConnectionState.DISCONNECTING);
            throw new IOException("Key agreement failed", e);
        }
    }
    
    /**
     * Process an incoming command message
     * Routes to appropriate handler based on message type and command
     * 
     * @param message the encrypted message from client
     * @throws IOException if processing fails
     */
    public void processMessage(String message) throws IOException {
        if (!session.canExecuteCommands()) {
            throw new IllegalStateException("Connection not ready for commands: state=" + session.getState());
        }
        
        try {
            // Decrypt message
            String decrypted = session.getSecretMessenger().decryptStr(message);
            
            // Parse command
            ProtocolParser.ParsedCommand cmd = ProtocolParser.parseCommand(decrypted);
            
            System.out.println("Processing command: " + cmd + " from " + session.getRemoteAddress());
            
            // TODO: Route to appropriate handler based on cmd.type
            // This will be implemented in Phase 3 with PacketHandler pattern
            
        } catch (Exception e) {
            System.err.println("Error processing message: " + e.getMessage());
            throw new IOException("Message processing failed", e);
        }
    }
    
    /**
     * Send an encrypted response to the client
     * @param message the plaintext message
     * @throws IOException if send fails
     */
    public void sendResponse(String message) throws IOException {
        if (!session.hasSecretMessenger()) {
            throw new IllegalStateException("No encryption available");
        }
        handler.sendEncrypted(message);
    }
    
    /**
     * Handle connection timeout or error
     * Transition to DISCONNECTING and clean up
     */
    public void disconnect() {
        if (session.getState() != ConnectionState.CLOSED) {
            session.transitionTo(ConnectionState.DISCONNECTING);
            session.forceState(ConnectionState.CLOSED);
            handler.close();
            System.out.println("Connection disconnected: " + session);
        }
    }
    
    // ==================== Helper Methods ====================
    
    private static String bytesToHexString(byte[] bytes) {
        StringBuilder hex = new StringBuilder();
        for (byte b : bytes) {
            hex.append(String.format("%02x", b));
        }
        return hex.toString();
    }
    
    private static byte[] hexStringToBytes(String hex) {
        int len = hex.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                                 + Character.digit(hex.charAt(i+1), 16));
        }
        return data;
    }
    
    // ==================== Accessors ====================
    
    public Session getSession() {
        return session;
    }
    
    public ConnectionHandler getHandler() {
        return handler;
    }
    
    public SocketChannel getSocketChannel() {
        return session.getSocketChannel();
    }
    
    @Override
    public String toString() {
        return String.format("ConnectionManager{session=%s}", session);
    }
}
