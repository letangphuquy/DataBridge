package Server.Session;

import java.nio.channels.SocketChannel;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.Collection;
import java.util.Optional;

/**
 * SessionManager - Thread-safe repository for managing all active sessions.
 * 
 * Responsibilities:
 * - Store and retrieve sessions by ID
 * - Track active sessions globally
 * - Provide safe iteration over active sessions
 * - Handle session registration and cleanup
 * 
 * Uses ConcurrentHashMap to avoid race conditions that plagued the old
 * HashMap-based activeUsers map.
 */
public class SessionManager {
    private final ConcurrentHashMap<Long, Session> sessions;
    private final AtomicLong sessionIdCounter;
    
    public SessionManager() {
        this.sessions = new ConcurrentHashMap<>();
        this.sessionIdCounter = new AtomicLong(1);
    }
    
    /**
     * Create and register a new session for an incoming connection
     * @param socketChannel the socket channel for the connection
     * @param remoteAddress the remote client address
     * @return the newly created session
     */
    public Session registerSession(SocketChannel socketChannel, String remoteAddress) {
        long sessionId = sessionIdCounter.incrementAndGet();
        Session session = new Session(sessionId, socketChannel, remoteAddress);
        sessions.put(sessionId, session);
        return session;
    }
    
    /**
     * Get a session by ID
     * @param sessionId the session ID
     * @return Optional containing the session if found
     */
    public Optional<Session> getSession(long sessionId) {
        return Optional.ofNullable(sessions.get(sessionId));
    }
    
    /**
     * Remove a session and perform cleanup
     * @param sessionId the session ID to remove
     * @return the removed session, or null if not found
     */
    public Session removeSession(long sessionId) {
        return sessions.remove(sessionId);
    }
    
    /**
     * Get all currently active sessions
     * Returns a snapshot, safe for iteration
     * @return collection of active sessions
     */
    public Collection<Session> getAllSessions() {
        return sessions.values();
    }
    
    /**
     * Get count of active sessions
     * @return number of currently active sessions
     */
    public int getActiveSessionCount() {
        return sessions.size();
    }
    
    /**
     * Find sessions that match a predicate (for cleanup, monitoring, etc.)
     * @param predicate function to test sessions
     * @return collection of matching sessions
     */
    public Collection<Session> findSessions(java.util.function.Predicate<Session> predicate) {
        return sessions.values().stream()
            .filter(predicate)
            .toList();
    }
    
    /**
     * Get a session by authenticated user ID
     * @param userId the user ID
     * @return Optional containing the session if found
     */
    public Optional<Session> getSessionByUserId(Long userId) {
        return sessions.values().stream()
            .filter(s -> s.getAuthenticatedUser() != null && 
                        s.getAuthenticatedUser().getUserID() == userId)
            .findFirst();
    }
    
    /**
     * Find all sessions for a specific user (may have multiple connections)
     * @param userId the user ID
     * @return collection of sessions for this user
     */
    public Collection<Session> getAllSessionsForUser(Long userId) {
        return sessions.values().stream()
            .filter(s -> s.getAuthenticatedUser() != null && 
                        s.getAuthenticatedUser().getUserID() == userId)
            .toList();
    }
    
    /**
     * Clean up idle or closed sessions
     * Safe to call periodically to reclaim resources
     * Uses a safe iteration pattern to handle concurrent modifications
     * @param idleTimeoutMs idle timeout in milliseconds
     * @return number of sessions cleaned up
     */
    public int cleanupIdleSessions(long idleTimeoutMs) {
        // Collect sessions to remove first to avoid concurrent modification
        java.util.List<Long> sessionsToRemove = new java.util.ArrayList<>();
        for (Session session : sessions.values()) {
            if (session.getIdleTimeMs() > idleTimeoutMs || !session.isConnected()) {
                sessionsToRemove.add(session.getSessionId());
            }
        }
        
        // Remove collected sessions
        int cleaned = 0;
        for (Long sessionId : sessionsToRemove) {
            if (sessions.remove(sessionId) != null) {
                cleaned++;
            }
        }
        return cleaned;
    }
    
    /**
     * Get summary statistics for monitoring
     */
    public String getStatsSummary() {
        int activeCount = 0;
        int authenticatedCount = 0;
        int handshakingCount = 0;
        
        for (Session session : sessions.values()) {
            if (session.isConnected()) activeCount++;
            if (session.isAuthenticated()) authenticatedCount++;
            if (session.getState() == ConnectionState.HANDSHAKING) handshakingCount++;
        }
        
        return String.format("Sessions: total=%d, active=%d, authenticated=%d, handshaking=%d",
            sessions.size(), activeCount, authenticatedCount, handshakingCount);
    }
}
