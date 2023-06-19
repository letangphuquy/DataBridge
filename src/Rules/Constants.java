package Rules;

public class Constants {
    private Constants() {}
    
    //General
    public static final CharSequence DELIMITER = new String(new char[] {29});//␝: group seperator

    //File
    public static final int BUFFER_SIZE = 1024 * 8;

    //Database
    public static final int ID_LENGTH = 30;
    public static final int USERNAME_MAX_LENGTH = 30;
    public static final int NAME_MAX_LENGTH = 60;

    //User
    public static final long DEFAULT_ID = Long.MIN_VALUE;

    //Chat
    public static final int MAX_CHAT_LENGTH = 500;
    
}
