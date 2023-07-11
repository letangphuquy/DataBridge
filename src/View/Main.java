package View;

import java.io.IOException;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) throws IOException {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ChatView();
            }
        });
    }
}