package View;

import java.awt.*;
import javax.swing.*;

import Rules.GUI;

class MainPanel extends JPanel {
    private void initialize() {
        setBackground(new Color(0x313338));
        setBounds(300, 0, 700, GUI.HEIGHT);
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