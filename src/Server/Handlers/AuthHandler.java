package Server.Handlers;

import Server.Session.Session;
import Server.Session.ConnectionState;
import Rules.ClientCode;
import Model.User;

/**
 * AuthHandler - Handles authentication-related commands
 * 
 * Commands:
 * - REGISTER: Create new user account
 * - LOGIN: Authenticate existing user
 * - LOGOUT: Terminate session
 * 
 * This handler transitions sessions from AUTHENTICATED to ACTIVE state.
 */
public class AuthHandler implements PacketHandler {
    
    @Override
    public String getType() {
        return "AUTH";
    }
    
    /**
     * Authentication commands don't require prior authentication
     * (obviously - you can't authenticate without this handler!)
     */
    @Override
    public boolean requiresAuthentication() {
        return false;
    }
    
    @Override
    public void handle(Session session, String command, String[] args) throws Exception {
        ClientCode.Command cmd = ClientCode.Command.valueOf(command);
        
        switch (cmd) {
            case REGISTER:
                handleRegister(session, args);
                break;
            case LOGIN:
                handleLogin(session, args);
                break;
            case LOGOUT:
                handleLogout(session, args);
                break;
            default:
                throw new IllegalArgumentException("Unknown AUTH command: " + command);
        }
    }
    
    /**
     * Handle user registration
     * Args: [username, password_hash]
     */
    private void handleRegister(Session session, String[] args) throws Exception {
        if (args.length < 2) {
            throw new IllegalArgumentException("REGISTER requires username and password");
        }
        
        String username = args[0];
        String passwordHash = args[1];
        
        // TODO: Implement registration logic
        // Placeholder: Send acknowledgment
        System.out.println("REGISTER command received for user: " + username);
    }
    
    /**
     * Handle user login
     * Args: [username, password_hash]
     */
    private void handleLogin(Session session, String[] args) throws Exception {
        if (args.length < 2) {
            throw new IllegalArgumentException("LOGIN requires username and password");
        }
        
        // Check if session is in proper state for authentication
        if (session.getState() != ConnectionState.AUTHENTICATED) {
            throw new IllegalStateException("Session not ready for login: " + session.getState());
        }
        
        String username = args[0];
        String passwordHash = args[1];
        
        // TODO: Implement login logic using existing Authenticator
        // Placeholder: Create a dummy user and transition to ACTIVE
        User user = new User(username);
        user.setIDs(1L, 1L); // Temporary IDs
        
        session.setAuthenticatedUser(user);
        session.transitionTo(ConnectionState.ACTIVE);
        System.out.println("User login processed: " + username + " (session: " + session.getSessionId() + ")");
    }
    
    /**
     * Handle user logout
     */
    private void handleLogout(Session session, String[] args) throws Exception {
        if (session.isAuthenticated()) {
            User user = session.getAuthenticatedUser();
            System.out.println("User logged out: " + user.getUsername());
            
            session.setAuthenticatedUser(null);
            session.transitionTo(ConnectionState.IDLE);
        }
    }
}
