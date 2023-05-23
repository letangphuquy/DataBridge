package Server;

import java.io.IOException;

import Model.Password;
import Model.User;
import Rules.*;
import Server.Database.Data;
public class Authenticator {
    private Authenticator() {}
    private static User login(ServerThread server, String username) throws IOException {
        //Read "AUTH LOGIN <username>" and triggered by ServerThread
        // username = String.format("%-" + DatabaseConst.RECIPIENT_ID_LENGTH + "s", username);
        String userID = Data.usernameToID.get(username);
        System.out.println("Hello " + userID);
        if (userID == null) {
            server.send(ServerCode.REJECT + " wrong username");
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
    private static User register(String username, String hashedPassword) {
        return null;
    }
    public static User process(ServerThread serverThread, ClientCode.Command command, String[] params) {
        try {
            switch (command) {
                case LOGIN:
                    return login(serverThread, params[0]);
                case REGISTER:
                    return register(params[0], params[1]);
                case LOGOUT:
                    return null;
                default:
                    return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
