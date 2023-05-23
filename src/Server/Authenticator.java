package Server;

import Rules.*;

public class Authenticator {
    private Authenticator() {}
    private static ServerCode login(String username, String hashedPassword) {
        return ServerCode.REJECT;
    }
    private static ServerCode register(String username, String hashedPassword) {
        return ServerCode.REJECT;
    }
    public static ServerCode process(ClientCode.Command command, String[] params) {
        switch (command) {
            case LOGIN:
                return login(params[0], params[1]);
            case REGISTER:
                return register(params[0], params[1]);
            case LOGOUT:
                return ServerCode.ACCEPT;
            default:
                return ServerCode.ERROR;
        }
    }
}
