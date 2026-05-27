package Server.Session;

/**
 * ConnectionState - Represents the lifecycle states of a client connection.
 * 
 * State transitions:
 * HANDSHAKING -> AUTHENTICATED -> ACTIVE -> DISCONNECTING -> CLOSED
 * HANDSHAKING -> DISCONNECTING -> CLOSED (if key exchange fails)
 * Any state -> DISCONNECTING -> CLOSED (on connection drop)
 */
public enum ConnectionState {
    /**
     * Initial state: Socket accepted, key exchange in progress
     */
    HANDSHAKING,
    
    /**
     * Key exchange completed, waiting for authentication
     */
    AUTHENTICATED,
    
    /**
     * User has logged in, can perform operations
     */
    ACTIVE,
    
    /**
     * Idle state - connection alive but inactive
     */
    IDLE,
    
    /**
     * Connection closing - cleanup in progress
     */
    DISCONNECTING,
    
    /**
     * Connection closed and resources released
     */
    CLOSED;
    
    /**
     * Check if a transition from this state to another is valid
     * @param nextState the target state
     * @return true if transition is allowed
     */
    public boolean canTransitionTo(ConnectionState nextState) {
        if (nextState == CLOSED) {
            // Can close from any state
            return this != CLOSED;
        }
        
        switch (this) {
            case HANDSHAKING:
                return nextState == AUTHENTICATED || nextState == DISCONNECTING;
            case AUTHENTICATED:
                return nextState == ACTIVE || nextState == DISCONNECTING;
            case ACTIVE:
                return nextState == IDLE || nextState == DISCONNECTING;
            case IDLE:
                return nextState == ACTIVE || nextState == DISCONNECTING;
            case DISCONNECTING:
                return nextState == CLOSED;
            case CLOSED:
                return false;
            default:
                return false;
        }
    }
    
    /**
     * Check if client can execute operations in this state
     * @return true if client can send commands
     */
    public boolean canExecuteCommands() {
        return this == ACTIVE;
    }
    
    /**
     * Check if client is considered connected
     * @return true if connection is still active
     */
    public boolean isConnected() {
        return this != CLOSED && this != DISCONNECTING;
    }
}
