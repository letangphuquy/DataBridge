package View;

import java.awt.GraphicsEnvironment;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException{
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Object[] fonts = ge.getAllFonts();
        for (Object font : fonts) {
            System.out.println(font);
        }
        new ChatView();
    }
}