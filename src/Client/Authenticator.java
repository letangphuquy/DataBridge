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
    
    private static boolean isLoginFailed(String[] parts) {
        if (!ServerCode.REJECT.toString().equals(parts[0])) {
            return false;
        }
        System.out.print("Login failed: ");
        Stream.of(parts).skip(1).forEach((s) -> System.out.print(s + " "));
        System.out.println();
        return true;
    }

    public static User login(String username, String password) throws IOException {
        Client client = Client.instance;
        client.send(ClientCode.Type.AUTH + " " + ClientCode.Command.LOGIN + " " + username);
        
        String response = client.read();
        String[] parts = response.split(" ");
        if (isLoginFailed(parts)) return null;

        assert ServerCode.DATA.toString().equals(parts[0]);
        String salt = parts[1];
        String hashedPassword = PasswordHasher.hash(password, salt);
        hashedPassword = "hashed_password";
        hashedPassword = String.format("%-64s", hashedPassword);
        System.out.println("Hashed password: " + hashedPassword);
        client.send(hashedPassword);
        
        response = client.read();
        parts = response.split(" ");
        if (isLoginFailed(parts)) return null;
        
        assert ServerCode.ACCEPT.toString().equals(parts[0]);
        response = client.read();
        parts = response.split((String) Constants.DELIMITER);
        return new User(parts);
    }
}
