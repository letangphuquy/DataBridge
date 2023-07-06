package Server;

import java.io.IOException;
import java.util.Arrays;

import Model.User;
import Rules.Constants;
import Rules.Relationship;
import Rules.ServerCode;
import Rules.ClientCode.Command;
import Server.Database.Data;
import Server.Database.DatabaseUpdater;

public class Socializer {
    private Socializer() {}
    private static final String D = String.valueOf(Constants.DELIMITER);
    
    public static void process(ServerThread serverThread, Command command, String[] params) throws IOException {
        int requestID = Integer.parseInt(params[params.length - 1]);
        params = Arrays.copyOf(params, params.length - 1);
        switch (command) {
            case EDIT:
                editProfile(serverThread, params, requestID);
                break;
            case SEARCH:
                searchUser(serverThread, params[0], requestID);
                break;
            case FRIEND:
                setRelationship(serverThread, params);
                break;
            default:
                break;
        }
    }
    
    private static void editProfile(ServerThread server, String[] params, int requestID) throws IOException {
        // NOTE: Data validation is done at client side
        User user = new User(params);
        long publicID = Long.parseLong(params[0]);
        long userID = Data.publicIDToRecipientID.get(publicID);
        user.setIDs(userID, publicID);
        DatabaseUpdater.updateUser(userID, user);
        server.user = user;
        server.send(ServerCode.ACCEPT + D + requestID);
    }
    
    private static void searchUser(ServerThread server, String name, int requestID) throws IOException {
        for (Long userID : Data.publicUsers) {
            User user = Data.users.get(userID);
            if (user.getName().contains(name)) {
                server.send(ServerCode.DATA + D + requestID + D + user.toString());
            }
        }
    }

    private static void setRelationship(ServerThread server, String[] params) throws IOException {
        long userAID = server.user.getUserID();
        long userBID = Long.parseLong(params[0]);
        Relationship attitude = Relationship.valueOf(params[1]);
        userBID = Data.publicIDToRecipientID.get(userBID);
        var pair = DatabaseUpdater.updateRelationship(userAID, userBID, attitude);
        var serverB = ServerThread.activeUsers.get(userBID);
        if (serverB != null) {
            serverB.send(ServerCode.USER + D + pair.toString());
        }
    }
}
