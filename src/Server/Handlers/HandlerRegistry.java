package Server.Handlers;

import Server.Session.Session;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * HandlerRegistry - Central dispatcher for command handlers
 * 
 * Replaces the giant switch-case statement with a registry pattern.
 * Handlers are registered and looked up dynamically.
 * 
 * Benefits:
 * - Eliminates switch-case anti-pattern
 * - Supports runtime handler registration
 * - Enables plugin-like handler loading
 * - Thread-safe for concurrent access
 */
public class HandlerRegistry {
    private final Map<String, PacketHandler> handlers;
    
    public HandlerRegistry() {
        this.handlers = new ConcurrentHashMap<>();
    }
    
    /**
     * Register a handler for a specific command type
     * @param type the command type (e.g., "AUTH", "CHAT")
     * @param handler the handler implementation
     */
    public void register(String type, PacketHandler handler) {
        handlers.put(type, handler);
        System.out.println("Registered handler for type: " + type);
    }
    
    /**
     * Get a handler for a command type
     * @param type the command type
     * @return Optional containing the handler if found
     */
    public Optional<PacketHandler> getHandler(String type) {
        return Optional.ofNullable(handlers.get(type));
    }
    
    /**
     * Dispatch a command to the appropriate handler
     * @param session the client session
     * @param type the command type
     * @param command the specific command
     * @param args command arguments
     * @throws Exception if handler not found or command fails
     */
    public void dispatch(Session session, String type, String command, String[] args) throws Exception {
        PacketHandler handler = getHandler(type)
            .orElseThrow(() -> new IllegalArgumentException("Unknown command type: " + type));
        
        // Check authentication if required
        if (handler.requiresAuthentication() && !session.isAuthenticated()) {
            throw new IllegalStateException("Authentication required for type: " + type);
        }
        
        handler.handle(session, command, args);
    }
    
    /**
     * Check if a handler is registered
     * @param type the command type
     * @return true if handler exists
     */
    public boolean hasHandler(String type) {
        return handlers.containsKey(type);
    }
    
    /**
     * Get count of registered handlers
     * @return number of handlers
     */
    public int getHandlerCount() {
        return handlers.size();
    }
    
    /**
     * Create and register all default handlers
     * This is called during server initialization
     */
    public static HandlerRegistry createDefault() {
        HandlerRegistry registry = new HandlerRegistry();
        
        // Register default handlers
        registry.register("AUTH", new AuthHandler());
        registry.register("USER", new UserHandler());
        registry.register("CHAT", new ChatHandler());
        registry.register("FILE", new FileHandler());
        
        System.out.println("Default handlers registered: " + registry.getHandlerCount());
        
        return registry;
    }
}
