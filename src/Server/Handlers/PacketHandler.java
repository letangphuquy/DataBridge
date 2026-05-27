package Server.Handlers;

import Server.Session.Session;
import Server.Session.ConnectionState;

/**
 * PacketHandler - Interface for command handlers
 * 
 * Implements Command Pattern and Strategy Pattern to replace the
 * massive switch-case statement in the original ServerThread.
 * 
 * This design allows:
 * - New command handlers without modifying existing code (OCP)
 * - Runtime handler registration
 * - Easy testing of individual handlers
 * - Clear separation of concerns
 */
public interface PacketHandler {
    /**
     * Handle a command from a client
     * 
     * @param session the client session
     * @param command the specific command (e.g., "LOGIN", "SEND")
     * @param args arguments for the command
     * @throws Exception if command processing fails
     */
    void handle(Session session, String command, String[] args) throws Exception;
    
    /**
     * Check if client can execute this handler's commands
     * Default implementation checks if client is authenticated
     * @return true if handler can be invoked
     */
    default boolean requiresAuthentication() {
        return true;
    }
    
    /**
     * Get the handler type (e.g., "AUTH", "CHAT")
     * @return the type name
     */
    String getType();
}
