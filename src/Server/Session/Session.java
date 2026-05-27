package Server.Session;

import Model.User;
import Model.SecretMessenger;
import java.nio.channels.SocketChannel;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Session - Encapsulates a client connection with thread-safe state management.
 * 
 * This class represents a single client's connection session, holding:
 * - Connection metadata (socket channel, remote address)
 * - Authentication state (user info, credentials)
 * - Cryptography state (shared secret for encryption)
 * - Session lifecycle state (handshaking, authenticated, active, etc.)
 * 
 * All state transitions are thread-safe using AtomicReference.
 * This ensures consistency across I/O threads and worker threads.
 */
public class Session {
    private final long sessionId;
    private final SocketChannel socketChannel;
    private final String remoteAddress;
    private final long createdAt;
    
    private final AtomicReference<ConnectionState> state;
    private final AtomicReference<User> authenticatedUser;
    private final AtomicReference<SecretMessenger> secretMessenger;
    
    private volatile long lastActivityTime;
    
    /**
     * Create a new session for an incoming connection
     * @param sessionId unique session identifier
     * @param socketChannel the NIO channel for this connection
     * @param remoteAddress remote client address for logging
     */
    public Session(long sessionId, SocketChannel socketChannel, String remoteAddress) {
        this.sessionId = sessionId;
        this.socketChannel = socketChannel;
        this.remoteAddress = remoteAddress;
        this.createdAt = System.currentTimeMillis();
        this.lastActivityTime = createdAt;
        
        this.state = new AtomicReference<>(ConnectionState.HANDSHAKING);
        this.authenticatedUser = new AtomicReference<>(null);
        this.secretMessenger = new AtomicReference<>(null);
    }
    
    // ==================== Accessors ====================
    
    public long getSessionId() {
        return sessionId;
    }
    
    public SocketChannel getSocketChannel() {
        return socketChannel;
    }
    
    public String getRemoteAddress() {
        return remoteAddress;
    }
    
    public long getCreatedAt() {
        return createdAt;
    }
    
    public long getLastActivityTime() {
        return lastActivityTime;
    }
    
    // ==================== State Management ====================
    
    public ConnectionState getState() {
        return state.get();
    }
    
    /**
     * Transition to a new state if allowed by current state.
     * Thread-safe atomic operation.
     * 
     * @param nextState target state
     * @return true if transition was successful, false if invalid
     */
    public boolean transitionTo(ConnectionState nextState) {
        ConnectionState currentState = state.get();
        if (currentState.canTransitionTo(nextState)) {
            boolean success = state.compareAndSet(currentState, nextState);
            if (success) {
                updateActivityTime();
            }
            return success;
        }
        return false;
    }
    
    /**
     * Force state transition without validation (for cleanup)
     * Use with caution - only during disconnection.
     */
    public void forceState(ConnectionState newState) {
        state.set(newState);
        updateActivityTime();
    }
    
    // ==================== User Authentication ====================
    
    public User getAuthenticatedUser() {
        return authenticatedUser.get();
    }
    
    /**
     * Set the authenticated user for this session
     * @param user the authenticated user, or null to clear
     */
    public void setAuthenticatedUser(User user) {
        authenticatedUser.set(user);
        updateActivityTime();
    }
    
    public boolean isAuthenticated() {
        return authenticatedUser.get() != null;
    }
    
    // ==================== Cryptography ====================
    
    public SecretMessenger getSecretMessenger() {
        return secretMessenger.get();
    }
    
    /**
     * Set the secret messenger for this session
     * @param messenger the initialized SecretMessenger
     */
    public void setSecretMessenger(SecretMessenger messenger) {
        secretMessenger.set(messenger);
        updateActivityTime();
    }
    
    public boolean hasSecretMessenger() {
        return secretMessenger.get() != null;
    }
    
    // ==================== Activity Tracking ====================
    
    public void updateActivityTime() {
        this.lastActivityTime = System.currentTimeMillis();
    }
    
    public long getIdleTimeMs() {
        return System.currentTimeMillis() - lastActivityTime;
    }
    
    // ==================== Validation ====================
    
    /**
     * Check if session can execute commands
     * @return true if session state allows command execution
     */
    public boolean canExecuteCommands() {
        return state.get().canExecuteCommands() && isAuthenticated();
    }
    
    /**
     * Check if session is still connected
     * @return true if socket channel is connected and state allows operations
     */
    public boolean isConnected() {
        return socketChannel.isOpen() && state.get().isConnected();
    }
    
    @Override
    public String toString() {
        return String.format("Session{id=%d, remote=%s, state=%s, user=%s, idle=%dms}",
            sessionId, remoteAddress, state.get(),
            authenticatedUser.get() != null ? authenticatedUser.get().getUsername() : "anonymous",
            getIdleTimeMs());
    }
}
