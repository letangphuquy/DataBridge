package Client;

import java.io.IOException;
import java.util.stream.Stream;

import Model.PasswordHasher;
import Model.User;
import Rules.ClientCode;
import Rules.ServerCode;

public class Authenticator {
    private Authenticator() {}
    
    private static void alertLoginFailed(String ... parts) {
        System.out.print("Login failed: ");
        Stream.of(parts).skip(1).forEach((s) -> System.out.print(s + " "));
        System.out.println();
    }

    public static User login(String username, String password) throws IOException {
        Client client = Client.instance;
        client.send(ClientCode.Type.AUTH + " " + ClientCode.Command.LOGIN + " " + username);
        String response = client.read();
        String[] parts = response.split(" ");
        if (ServerCode.REJECT.toString().equals(parts[0])) {
            alertLoginFailed(parts);
            return null;
        }
        assert ServerCode.DATA.toString().equals(parts[0]);
        String salt = parts[1];
        String hashedPassword = PasswordHasher.hash(password, salt);
        client.send(hashedPassword);
        response = client.read();
        if (ServerCode.REJECT.toString().equals(parts[0])) {
            alertLoginFailed(parts);
            return null;
        }
        assert ServerCode.ACCEPT.toString().equals(parts[0]);
        response = client.read();
        parts = response.split(" ");
        return new User(parts);
    }
}
