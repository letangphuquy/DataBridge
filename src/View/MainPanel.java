package View;

import java.awt.*;
import javax.swing.*;

import Rules.GUI;

class MainPanel extends JPanel {
    private void initialize() {
        setBackground(new Color(0x313338));
        setBounds(280, 0, 705, GUI.HEIGHT);
        setLayout(null);
    }

    MainPanel() {
        initialize();
    }

    // public MainPanel(LayoutManager manager) {
    //     super(manager);
    //     initialize();
    // }
}