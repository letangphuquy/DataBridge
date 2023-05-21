package Model;

public class Password {
    private String username, salt, hashedPassword;
    public Password(String username, String salt, String hashedPassword) {
        this.username = username;
        this.salt = salt;
        this.hashedPassword = hashedPassword;
    }
}
