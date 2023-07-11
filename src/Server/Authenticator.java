package Server;

import java.io.IOException;
import java.util.ArrayList;

import Model.Message;
import Model.Password;
import Model.RandomGenerator;
import Model.Recipient;
import Model.TypesConverter;
import Model.User;
import Rules.*;
import Server.Database.Data;
import Server.Database.DatabaseInserter;
public class Authenticator {
    private Authenticator() {}
    private static void onLogin(ServerThread server, User user) throws IOException {
        //TODO: send user data
        server.user = user;
        long userID = user.getUserID();
        ServerThread.activeUsers.put(userID, server);
        server.send(ServerCode.ACCEPT.toString()); //sending ACCEPT to differs from REJECT in case of wrong password
        server.send(user.toString());

        var friendsList = Data.friendsOf.get(userID);
        int friendsCount = friendsList == null ? 0 : friendsList.size();
        server.send(ServerCode.DATA + " FRIENDS " + friendsCount);
        if (friendsCount > 0)
            for (long friendID : friendsList)
                server.send(Data.users.get(friendID).toString());
        
        var groupsList = Data.groupsOf.get(userID);
        int groupsCount = groupsList == null ? 0 : groupsList.size();
        server.send(ServerCode.DATA + " GROUPS " + groupsCount);
        if (groupsCount > 0)
            for (long groupID : groupsList)
                server.send(Data.groups.get(groupID).toString());

        groupsList.add(userID);
        ArrayList<Message> messages = new ArrayList<>();
        for (long groupID : groupsList) {
            for (var message : Data.recipients.get(groupID).getMessages()) {
                //NOTE: send publicID instead of recipientID (important!)
                message.setSenderID(Data.recipientIDToPublicID.get(message.getSenderID()));
                message.setReceiverID(Data.recipientIDToPublicID.get(message.getReceiverID()));
                messages.add(message); //already is a sub class instance
            }
        }
        server.send(ServerCode.DATA + " MESSAGES " + messages.size());
        for (var message : messages)
            server.send(message.toString());
    }

    /*
     * Login procedure:
     * 1. Check for sent username's existence
     * 2. Send salt
     * 3. Receive hashed password, verify
     * 4. Send user data (if success)
     * Important note: allow "blocking" communication
     */
    // private static String D = (String) Constants.DELIMITER;
    private static void login(ServerThread server, String username) throws IOException {
        //Read "AUTH LOGIN <username>" and triggered by ServerThread
        if (!Data.usernameToID.containsKey(username)) {
            server.send(ServerCode.REJECT + " username not exists");
            return ;
        }
        long userID = Data.usernameToID.get(username);
        Password password = Data.passwordOf.get(username);
        server.send(ServerCode.DATA + " " + password.getSalt());
        String hashedPassword = server.read();
        if (!password.verify(hashedPassword)) {
            server.send(ServerCode.REJECT + " wrong password");
            return ;
        }
        User user = Data.users.get(userID);
        onLogin(server, user);
    }
    /*
     * Register procedure: more or less the same as login
     * auto login after register
     */

    private static void register(ServerThread server, String username) throws IOException {
        //Read "AUTH REGISTER <username>" and triggered by ServerThread
        if (Data.usernameToID.containsKey(username)) {
            server.send(ServerCode.REJECT + " username already exists");
            return ;
        }
        byte[] salt = RandomGenerator.randomSalt();
        String saltString = TypesConverter.bytesToString(salt);
        server.send(ServerCode.DATA + " " + saltString);
        
        String hashedPassword = server.read();
        Password password = new Password(username, saltString, hashedPassword);
        User user = new User(username);
        user.setIDs(Recipient.randomRecipient());
        DatabaseInserter.addUser(user, password);

        onLogin(server, user);
    }

    private static void logout(ServerThread server) {
        //Read "AUTH LOGOUT" and triggered by ServerThread
        FileProcessor.filesOnReceiving.clear();
        ServerThread.activeUsers.remove(server.user.getUserID());
        server.user = null;
    }

    public static void process(ServerThread serverThread, ClientCode.Command command, String[] params) {
        try {
            switch (command) {
                case LOGIN:
                    login(serverThread, params[0]);
                    break;
                case REGISTER:
                    register(serverThread, params[0]);
                    break;
                case LOGOUT:
                    logout(serverThread);
                    break;
                default:;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
