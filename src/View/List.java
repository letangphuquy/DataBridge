import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class List {
    private JPanel panel;
    List() {
        // Pan2 - Center
        panel = new JPanel();
            panel.setBackground(new Color(0x2b2d31));
            panel.setBounds(70, 0, 230, 750);
            panel.setLayout(null);
    }
    public void select(int y) {
        JPanel line = new JPanel();
            line.setBackground(new Color(0xedf2fa));
            line.setBounds(0, y*50+50, 230, 50);
        panel.add(line);
    }
    public JPanel getPanel() {
        return panel;
    }
    public void add(JPanel user) {
        panel.add(user);
    }
    public void add(JButton plus) {
        panel.add(plus);
    }
    public void add(Partners partners) {
        panel.add(partners.getPanel());
    }
}