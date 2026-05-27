package Server.Handlers;

import Server.Session.Session;
import Rules.ClientCode;

/**
 * FileHandler - Handles file-related commands
 * 
 * Commands:
 * - UPLOAD: Upload a file
 * - DOWNLOAD: Download a file
 * - VIEW: View file info
 * - PUBLISH: Make file public
 * - SEARCH: Search files
 * 
 * Requires authentication
 */
public class FileHandler implements PacketHandler {
    
    @Override
    public String getType() {
        return "FILE";
    }
    
    @Override
    public void handle(Session session, String command, String[] args) throws Exception {
        ClientCode.Command cmd = ClientCode.Command.valueOf(command);
        
        switch (cmd) {
            case UPLOAD:
                handleUpload(session, args);
                break;
            case DOWNLOAD:
                handleDownload(session, args);
                break;
            case VIEW:
                handleView(session, args);
                break;
            case PUBLISH:
                handlePublish(session, args);
                break;
            case SEARCH:
                handleSearch(session, args);
                break;
            default:
                throw new IllegalArgumentException("Unknown FILE command: " + command);
        }
    }
    
    private void handleUpload(Session session, String[] args) throws Exception {
        if (args.length < 1) {
            throw new IllegalArgumentException("UPLOAD requires filename");
        }
        // TODO: Implement upload file logic
        System.out.println("FILE UPLOAD");
    }
    
    private void handleDownload(Session session, String[] args) throws Exception {
        if (args.length < 1) {
            throw new IllegalArgumentException("DOWNLOAD requires filename");
        }
        // TODO: Implement download file logic
        System.out.println("FILE DOWNLOAD");
    }
    
    private void handleView(Session session, String[] args) throws Exception {
        if (args.length < 1) {
            throw new IllegalArgumentException("VIEW requires filename");
        }
        // TODO: Implement view file logic
        System.out.println("FILE VIEW");
    }
    
    private void handlePublish(Session session, String[] args) throws Exception {
        if (args.length < 1) {
            throw new IllegalArgumentException("PUBLISH requires filename");
        }
        // TODO: Implement publish file logic
        System.out.println("FILE PUBLISH");
    }
    
    private void handleSearch(Session session, String[] args) throws Exception {
        if (args.length < 1) {
            throw new IllegalArgumentException("SEARCH requires search term");
        }
        // TODO: Implement search files logic
        System.out.println("FILE SEARCH");
    }
}
