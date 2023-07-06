package View;

import javax.swing.*;
import java.awt.*;

public class TestUnit {
    public static void main(String[] args) {
        // Create a JFrame
        JFrame frame = new JFrame("Background Image Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        
        // Create a JPanel to hold the content
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                
                // Load the background image
                ImageIcon backgroundImage = new ImageIcon("E:\\Computer Science\\Projects\\DataBridge\\images\\BG.jpg");
                
                // Draw the background image
                g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), null);
            }
        };
        
        // Set the panel as the content pane of the frame
        frame.setContentPane(panel);
        
        // Make the frame visible
        frame.setVisible(true);
    }
}
