package Client;

import java.io.IOException;
import java.util.Arrays;

import Model.User;
import Rules.ClientCode.*;
import Rules.Constants;
import Rules.Relationship;


public class Socializer {
    private Socializer() {}
    private static Client client = Client.instance;
    private static final String D = String.valueOf(Constants.DELIMITER);

    public static void editProfile(User user) throws IOException {
        client.sendRequest(Type.USER + D + Command.EDIT + D + user.toString());
    }

    public static void search(String name) throws IOException {
        client.sendRequest(Type.USER + D + Command.SEARCH + D + name);
    }
    
    public static void setRelationship(long userID, Relationship status) throws IOException {
        client.send(Type.USER + D + Command.FRIEND + D + userID + D + status);
    }

    public static void process(Command reqCommand, String[] requestPart, String[] responsePart) {
        switch (reqCommand) {
            case EDIT:
                editProfileAccepted(requestPart);
                break;
            case SEARCH:
                searchResultReceived(responsePart);
            default:
                System.out.println("Invalid request");
        }
    }
    
    private static void editProfileAccepted(String[] parts) {
        client.user = User.parse(parts);
    }

    //TODO: Combine two functions below with GUI (MVC model)
    private static void searchResultReceived(String[] parts) {
        User user = User.parse(parts);
        System.out.println("Search result: " + user);
    }

    public static void relationshipChanged(String[] parts) {
        System.out.println(Arrays.copyOf(parts, parts.length));
    }
}
