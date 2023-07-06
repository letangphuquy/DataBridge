package View;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class Info {
    private JPanel panel;
    Info() {
        // Pan3 - Left
        panel = new JPanel();
            panel.setBackground(new Color(0x313338));
            panel.setBounds(300, 0, 700, 750);
            panel.setLayout(null);
    }
    public JPanel getPanel() {
        return panel;
    }
    public void add(Mess mess) {
        panel.add(mess.getPanel());
    }
    public void add(JPanel panel2) {
        panel.add(panel2);
    }
    public void add(JLabel line) {
        panel.add(line);
    }

}