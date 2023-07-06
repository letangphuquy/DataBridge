package View;

import javax.swing.*;
import java.awt.*;

public class Test {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Hover Panel Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Create a HoverPanel and add it to the frame
        HoverPanel hoverPanel = new HoverPanel();
        hoverPanel.setPreferredSize(new Dimension(200, 100));
        frame.add(hoverPanel, BorderLayout.CENTER);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
