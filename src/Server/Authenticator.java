package Server;

import java.io.IOException;

import Model.Password;
import Model.RandomGenerator;
import Model.TypesConverter;
import Model.User;
import Rules.*;
import Server.Database.Data;
import Server.Database.DatabaseUpdater;
public class Authenticator {
    private Authenticator() {}
    /*
     * Login procedure:
     * 1. Check for sent username's existence
     * 2. Send salt
     * 3. Receive hashed password, verify
     * 4. Send user data (if success)
     */
    // private static String D = (String) Constants.DELIMITER;
    private static User login(ServerThread server, String username) throws IOException {
        //Read "AUTH LOGIN <username>" and triggered by ServerThread
        String userID = Data.usernameToID.get(username);
        if (userID == null) {
            server.send(ServerCode.REJECT + " username not exists");
            return null;
        }
        Password password = Data.passwordOf.get(username);
        server.send(ServerCode.DATA + " " + password.getSalt());
        String hashedPassword = server.read();
        if (!password.verify(hashedPassword)) {
            server.send(ServerCode.REJECT + " wrong password");
            return null;
        }
        server.send(ServerCode.ACCEPT.toString());
        User user = Data.users.get(userID);
        server.send(user.toString());
        return user;
    }
    /*
     * Register procedure: more or less the same as login
     */
    private static User register(ServerThread server, String username) throws IOException {
        //Read "AUTH REGISTER <username>" and triggered by ServerThread
        String userID = Data.usernameToID.get(username);
        if (userID != null) {
            server.send(ServerCode.REJECT + " username already exists");
            return null;
        }
        byte[] salt = RandomGenerator.randomSalt();
        String saltString = TypesConverter.bytesToString(salt);
        server.send(ServerCode.DATA + " " + saltString);
        
        String hashedPassword = server.read();
        Password password = new Password(username, saltString, hashedPassword);
        User user = new User(username);
        String recipientID = null;
        do {
            recipientID = RandomGenerator.randomReadableString(Constants.ID_LENGTH);
        } while (Data.allReIDs.contains(recipientID));
        System.out.println("Generated recipient ID: " + recipientID);
        user.setRecipientID(recipientID);
        DatabaseUpdater.addUser(user, password);

        server.send(user.toString());
        return user;
    }

    private static User logout(ServerThread server) {
        //Read "AUTH LOGOUT" and triggered by ServerThread
        FileProcessor.filesOnReceiving.clear();
        return server.user = null;
    }

    public static User process(ServerThread serverThread, ClientCode.Command command, String[] params) {
        try {
            switch (command) {
                case LOGIN:
                    return login(serverThread, params[0]);
                case REGISTER:
                    return register(serverThread, params[0]);
                case LOGOUT:
                    return logout(serverThread);
                default:
                    return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
