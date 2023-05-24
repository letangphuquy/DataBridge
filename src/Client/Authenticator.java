package Client;

import java.io.IOException;
import java.util.stream.Stream;

import Model.PasswordHasher;
import Model.User;
import Rules.ClientCode;
import Rules.Constants;
import Rules.ServerCode;

public class Authenticator {
    private Authenticator() {}
    
    private static boolean isAttemptFailed(String action, String[] parts) {
        if (!ServerCode.REJECT.toString().equals(parts[0])) {
            return false;
        }
        System.out.print(action + " failed: ");
        Stream.of(parts).skip(1).forEach((s) -> System.out.print(s + " "));
        System.out.println();
        return true;
    }

    public static User login(String username, String password) throws IOException {
        Client client = Client.instance;
        client.send(ClientCode.Type.AUTH + " " + ClientCode.Command.LOGIN + " " + username);
        
        String response = client.read();
        String[] parts = response.split(" ");
        if (isAttemptFailed("Login", parts)) return null;

        assert ServerCode.DATA.toString().equals(parts[0]);
        String salt = parts[1];
        String hashedPassword = PasswordHasher.hash(password, salt);
        System.out.println("Hashed password: " + hashedPassword);
        client.send(hashedPassword);
        
        response = client.read();
        parts = response.split(" ");
        if (isAttemptFailed("Login", parts)) return null;
        
        assert ServerCode.ACCEPT.toString().equals(parts[0]);
        response = client.read();
        parts = response.split((String) Constants.DELIMITER);
        return new User(parts);
    }

    public static User register(String username, String password) throws IOException {
        Client client = Client.instance;
        client.send(ClientCode.Type.AUTH + " " + ClientCode.Command.REGISTER + " " + username);

        String response = client.read();
        String[] parts = response.split(" ");
        if (isAttemptFailed("Register", parts)) return null;

        assert ServerCode.DATA.toString().equals(parts[0]);
        String saltString = parts[1];
        String hashedPassword = PasswordHasher.hash(password, saltString);
        client.send(hashedPassword);

        response = client.read();
        parts = response.split((String) Constants.DELIMITER);
        return new User(parts);
    }
}