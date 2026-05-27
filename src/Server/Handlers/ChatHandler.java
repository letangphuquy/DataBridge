package Server.Handlers;

import Server.Session.Session;
import Rules.ClientCode;

/**
 * ChatHandler - Handles chat/messaging commands
 * 
 * Commands:
 * - SEND: Send a message
 * - CREATE: Create a group chat
 * - ADD: Add user to group
 * - REMOVE: Remove user from group
 * - PROMOTE/DEMOTE: Manage group roles
 * 
 * Requires authentication
 */
public class ChatHandler implements PacketHandler {
    
    @Override
    public String getType() {
        return "CHAT";
    }
    
    @Override
    public void handle(Session session, String command, String[] args) throws Exception {
        ClientCode.Command cmd = ClientCode.Command.valueOf(command);
        
        switch (cmd) {
            case SEND:
                handleSend(session, args);
                break;
            case CREATE:
                handleCreate(session, args);
                break;
            case ADD:
                handleAdd(session, args);
                break;
            case REMOVE:
                handleRemove(session, args);
                break;
            case PROMOTE:
                handlePromote(session, args);
                break;
            case DEMOTE:
                handleDemote(session, args);
                break;
            default:
                throw new IllegalArgumentException("Unknown CHAT command: " + command);
        }
    }
    
    private void handleSend(Session session, String[] args) throws Exception {
        if (args.length < 2) {
            throw new IllegalArgumentException("SEND requires recipient and message");
        }
        // TODO: Implement send message logic
        System.out.println("CHAT SEND");
    }
    
    private void handleCreate(Session session, String[] args) throws Exception {
        if (args.length < 1) {
            throw new IllegalArgumentException("CREATE requires group name");
        }
        // TODO: Implement create group logic
        System.out.println("CHAT CREATE");
    }
    
    private void handleAdd(Session session, String[] args) throws Exception {
        if (args.length < 2) {
            throw new IllegalArgumentException("ADD requires group and user");
        }
        // TODO: Implement add user logic
        System.out.println("CHAT ADD");
    }
    
    private void handleRemove(Session session, String[] args) throws Exception {
        if (args.length < 2) {
            throw new IllegalArgumentException("REMOVE requires group and user");
        }
        // TODO: Implement remove user logic
        System.out.println("CHAT REMOVE");
    }
    
    private void handlePromote(Session session, String[] args) throws Exception {
        if (args.length < 2) {
            throw new IllegalArgumentException("PROMOTE requires user and group");
        }
        // TODO: Implement promote logic
        System.out.println("CHAT PROMOTE");
    }
    
    private void handleDemote(Session session, String[] args) throws Exception {
        if (args.length < 2) {
            throw new IllegalArgumentException("DEMOTE requires user and group");
        }
        // TODO: Implement demote logic
        System.out.println("CHAT DEMOTE");
    }
}
