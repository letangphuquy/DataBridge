package Client;

import java.io.IOException;

import javax.swing.JOptionPane;

import Model.PasswordHasher;
import Model.User;
import Rules.ClientCode;
import Rules.Constants;
import Rules.ServerCode;

public class Authenticator {
    private Authenticator() {}
    private static final int TIMEOUT = 5 * 1000;
    private static String D = (String) Constants.DELIMITER;
    private static Client client = Client.instance;

    private static boolean isAttemptFailed(String action, String[] parts) {
        if (!ServerCode.REJECT.toString().equals(parts[0])) {
            return false;
        }
        String msg = "";
        for (int i = 1; i < parts.length; i++) msg += parts[i] + " ";
        JOptionPane.showMessageDialog(client.authView, msg, action + " failed", JOptionPane.ERROR_MESSAGE);
        return true;
    }

    //Quick note: "blocking" communication
    private static void receiveUserData() throws IOException {
        String response = client.read();
        String[] parts = response.split((String) Constants.DELIMITER);
        client.user = new User(parts);
        client.user.setIDs(Constants.DEFAULT_ID, Long.parseLong(parts[0])); // important for messaging
        client.serverListener.start();
        //TODO: receive information about friends, chat messages, files, etc.
    }

    /*
     * Note: because user hasn't logged in, we can communicate in a "blocking" way
     * Login procedure:
     * 1. Send username
     * 2. Receive salt
     * 3. Send hashed password
     * 4. Receive user data
     */
    public static boolean login(String username, String password) throws IOException {
        client.send(ClientCode.Type.AUTH + D + ClientCode.Command.LOGIN + D + username);
        
        String response = client.read();
        String[] parts = response.split(" ");
        if (isAttemptFailed("Login", parts)) return false;

        assert ServerCode.DATA.toString().equals(parts[0]);
        String salt = parts[1];
        String hashedPassword = PasswordHasher.hash(password, salt);
        System.out.println("Hashed password: " + hashedPassword);
        client.send(hashedPassword);
        
        response = client.read();
        parts = response.split(" ");
        if (isAttemptFailed("Login", parts)) return false;
        
        assert ServerCode.ACCEPT.toString().equals(parts[0]);
        receiveUserData();
        return true;
    }

    /*
     * Register procedure: more or less the same as login
     */
    public static boolean register(String username, String password) throws IOException {
        client.send(ClientCode.Type.AUTH + D + ClientCode.Command.REGISTER + D + username);

        String response = client.read();
        System.out.println("Response: " + response);
        String[] parts = response.split(" ");
        if (isAttemptFailed("Register", parts)) return false;

        assert ServerCode.DATA.toString().equals(parts[0]);
        String saltString = parts[1];
        String hashedPassword = PasswordHasher.hash(password, saltString);
        client.send(hashedPassword);

        response = client.read();
        parts = response.split(" ");
        assert ServerCode.ACCEPT.toString().equals(parts[0]);
        
        receiveUserData();
        return true;
    }

    public static void logout() throws IOException {
        System.out.println("Logging out");
        // add some delays for ongoing tasks to finish
        try {
            Thread.sleep(TIMEOUT);
        } catch (InterruptedException e) {
            System.out.println("Why would someone interrupt my sleep?");
            client.debug(e);
        } 
        for (var thread : client.independentThreads)
            try {
                System.out.println("Waiting for thread " + thread.getName() + " to join");
                thread.join();
            } catch (InterruptedException e) {
                System.out.println("Could not join thread " + thread.getName());
                client.debug(e);
            }
        client.independentThreads.clear();
        System.out.println("Resolved all tasks");
        // client.serverListener.interrupt();
        client.send(ClientCode.Type.AUTH + D + ClientCode.Command.LOGOUT);
        client.user = null;
        client.requests.clear();
        System.out.println("Removed cached data");
    }
}
