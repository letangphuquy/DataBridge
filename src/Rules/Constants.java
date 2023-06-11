package Rules;

public class Constants {
    private Constants() {}
    
    //General
    public static final CharSequence DELIMITER = new String(new char[] {29});//␝: group seperator

    //File
    public static final int BUFFER_SIZE = 1024 * 8;

    //Database
    public static final int RECIPIENT_ID_LENGTH = 30;
    public static final int USERNAME_MAX_LENGTH = 30;
    public static final int NAME_MAX_LENGTH = 60;
    
}
