package Server.Handlers;

import Server.Session.Session;
import Rules.ClientCode;

/**
 * UserHandler - Handles user-related commands
 * 
 * Commands:
 * - SEARCH: Find users by name
 * - VIEW: View user profile
 * - EDIT: Edit user profile
 * - FRIEND: Manage friend relationships
 * 
 * Requires authentication
 */
public class UserHandler implements PacketHandler {
    
    @Override
    public String getType() {
        return "USER";
    }
    
    @Override
    public void handle(Session session, String command, String[] args) throws Exception {
        try {
            ClientCode.Command cmd = ClientCode.Command.valueOf(command);
            
            switch (cmd) {
                case SEARCH:
                    handleSearch(session, args);
                    break;
                case VIEW:
                    handleView(session, args);
                    break;
                case EDIT:
                    handleEdit(session, args);
                    break;
                case FRIEND:
                    handleFriend(session, args);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown USER command: " + command);
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid or unknown USER command: '" + command + "'", e);
        }
    }
    
    private void handleSearch(Session session, String[] args) throws Exception {
        if (args.length < 1) {
            throw new IllegalArgumentException("SEARCH requires a search term");
        }
        String searchTerm = args[0];
        // TODO: Implement user search logic
        System.out.println("USER SEARCH: " + searchTerm);
    }
    
    private void handleView(Session session, String[] args) throws Exception {
        if (args.length < 1) {
            throw new IllegalArgumentException("VIEW requires a user ID");
        }
        long userId = Long.parseLong(args[0]);
        // TODO: Implement user profile view logic
        System.out.println("USER VIEW: " + userId);
    }
    
    private void handleEdit(Session session, String[] args) throws Exception {
        if (args.length < 1) {
            throw new IllegalArgumentException("EDIT requires data");
        }
        // TODO: Implement user profile edit logic
        System.out.println("USER EDIT");
    }
    
    private void handleFriend(Session session, String[] args) throws Exception {
        if (args.length < 2) {
            throw new IllegalArgumentException("FRIEND requires user ID and status");
        }
        long userId = Long.parseLong(args[0]);
        int status = Integer.parseInt(args[1]);
        // TODO: Implement friend relationship logic
        System.out.println("USER FRIEND: " + userId + " status=" + status);
    }
}
