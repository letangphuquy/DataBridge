package Model;

public class Password {
    private String username, salt, hashedPassword;
    public Password(String username, String salt, String hashedPassword) {
        this.username = username;
        this.salt = salt;
        this.hashedPassword = hashedPassword;
    }
    public String getSalt() { return salt; }
    public boolean verify(String password) {
        return hashedPassword.equals(password);
    }
    //testing purposes
    public String toString() {
        return username + " " + salt + " " + hashedPassword;
    }
}
