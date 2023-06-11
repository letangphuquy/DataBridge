package Rules;

/*
 * ClientCode: 
 * Rules for communication from client to server (via sockets) (aka. requests)
 * TYPE <space> COMMAND <space> DATA
 * Special case: [TYPE=KEY] <space> DATA
 */

public final class ClientCode {
    private ClientCode() {}

    public enum Type {
        KEY,
        AUTH, USER, FILE, CHAT
    }
    
    public enum Command {
        REGISTER, LOGIN, LOGOUT, //AUTH
        FRIEND, //USER
        UPLOAD, DOWNLOAD, VIEW, //FILE
        ADD, REMOVE, SEND, //CHAT
        CREATE, EDIT, SEARCH // GENERAL
    }

    /*
     * List of all possible commands:
     * KEY <_> p^a (or p^b) mod n
     * AUTH <_> REGISTER, LOGIN <_> USERNAME <_> HASHED_PASSWORD
     * AUTH <_> LOGOUT
     * USER <_> FRIEND <_> USERNAME <_> STATUS (-1,0,1)
     * FILE <_> UPLOAD <_> FILENAME <_> METADATA, then FILEDATA (data may be sent in chunks of 8KB)
     * FILE <_> DOWNLOAD, VIEW <_> FILENAME
     * FILE <_> CREATE <_> FILENAME <_> METADATA (such as path, size, ...)
     * CHAT <_> ADD, REMOVE <_> USERNAME
     * CHAT <_> SEND <_> RECIPIENT <_> MESSAGE / FILE_LINK
     * CHAT <_> CREATE <_> GROUP_NAME <_> USERNAME1, USERNAME2, USERNAME3, ...
     * CHAT <_> EDIT <_> GROUP_NAME 
     * USER <_> EDIT <_> KEY_1 : VALUE_1 , KEY_2 : VALUE_2 , ... (maybe splitted to multiple messages)
     * USER <_> SEARCH <_> USERNAME
     * FILE <_> SEARCH <_> FILENAME
     * CHAT <_> SEARCH <_> GROUP_NAME
     */
}
