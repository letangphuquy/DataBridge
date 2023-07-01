package Model;

public class Password implements DatabaseModel {
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
    public Object[] toObjectArray() {
        return new Object[] {username, salt, hashedPassword};
    }
}
